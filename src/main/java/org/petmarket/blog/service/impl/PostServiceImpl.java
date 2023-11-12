package org.petmarket.blog.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.posts.BlogPostRequestDto;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.dto.posts.BlogPostTranslationRequestDto;
import org.petmarket.blog.entity.BlogCategory;
import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.blog.entity.Post;
import org.petmarket.blog.entity.PostTranslations;
import org.petmarket.blog.mapper.BlogPostMapper;
import org.petmarket.blog.repository.PostRepository;
import org.petmarket.blog.service.CategoryService;
import org.petmarket.blog.service.PostService;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.service.LanguageService;
import org.petmarket.options.service.OptionsService;
import org.petmarket.users.service.UserService;
import org.petmarket.utils.TransliterateUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private static final int AVERAGE_READING_WORDS_AMOUNT = 150;
    private final PostRepository postRepository;
    private final BlogPostMapper postMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final OptionsService optionsService;
    private final LanguageService languageService;
    private final TransliterateUtils transliterateUtils;

    @Transactional
    @Override
    public BlogPostResponseDto get(Long id, String langCode) {
        Post post = findById(id);
        post.setCategories(getFilteredCategories(post, langCode));
        post.setTranslations(getTranslation(id, langCode));
        post.setComments(post.getComments().stream()
                .filter(comment -> comment.getStatus().equals(CommentStatus.APPROVED))
                .collect(Collectors.toList()));
        return postMapper.toDto(post);
    }

    @Override
    public List<BlogPostResponseDto> getAll(Pageable pageable, String langCode) {

        return postRepository.findAll(pageable).stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    @Override
    public BlogPostResponseDto updateById(Long id, String langCode, BlogPostRequestDto requestDto) {
        Post post = findById(id);
        for (PostTranslations translation : post.getTranslations()) {
            if (translation.getLangCode().equals(checkedLang(langCode))) {
                translation.setTitle(requestDto.getTitle());
                translation.setText(requestDto.getText());
                translation.setShortText(truncateStringTo500Characters(requestDto.getText()));
            }
        }
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Can't find Blog Post id: " + id)
        );
    }

    @Transactional
    @Override
    public BlogPostResponseDto savePost(BlogPostRequestDto requestDto,
                                        Authentication authentication) {
        Post post = createPost(requestDto, authentication);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Transactional
    @Override
    public BlogPostResponseDto addTranslation(Long postId,
                                              String langCode,
                                              BlogPostTranslationRequestDto requestDto) {
        Post post = findById(postId);
        List<PostTranslations> translations = post.getTranslations();
        if (translations.stream()
                .anyMatch(t -> t.getLangCode()
                        .equals(checkedLang(langCode)))) {
            throw new ItemNotUpdatedException(langCode + " translation is already exist");
        } else {
            PostTranslations translation = PostTranslations.builder()
                    .post(post)
                    .title(requestDto.getTitle())
                    .text(requestDto.getText())
                    .langCode(langCode)
                    .shortText(truncateStringTo500Characters(requestDto.getText()))
                    .build();
            translations.add(translation);
            post.setTranslations(translations);
            postRepository.save(post);
        }
        return postMapper.toDto(post);
    }

    @Override
    public List<BlogPostResponseDto> getAllByCategory(String langCode,
                                                      Long categoryId,
                                                      Pageable pageable) {
        List<Post> allPosts;
        if (categoryId != null) {
            allPosts = findAllByBlogCategoryId(categoryId, pageable);
        } else {
            allPosts = postRepository.findAll(pageable).getContent();
        }
        return allPosts.stream()
                .filter(post -> post.getStatus().equals(Post.Status.PUBLISHED))
                .map(post -> {
                    post.setCategories(getFilteredCategories(post, langCode));
                    post.setComments(post.getComments().stream()
                            .filter(blogComment -> blogComment.getStatus().equals(CommentStatus.APPROVED))
                            .collect(Collectors.toList()));
                    post.setTranslations(getTranslation(post.getId(), langCode));
                    return post;
                })
                .map(postMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public BlogPostResponseDto updateStatus(Long postId, Post.Status status) {
        Post post = findById(postId);
        post.setStatus(status);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Transactional
    @Override
    public BlogPostResponseDto save(BlogPostRequestDto requestDto) {
        return null;
    }

    private String truncateStringTo500Characters(String input) {
        if (input.length() <= 500) {
            return input;
        } else {
            return input.substring(0, 500);
        }
    }

    private PostTranslations createPostTranslation(Post post, BlogPostRequestDto requestDto, String langCode) {
        return PostTranslations.builder()
                .post(post)
                .title(requestDto.getTitle())
                .text(requestDto.getText())
                .langCode(langCode)
                .shortText(truncateStringTo500Characters(requestDto.getText()))
                .build();
    }

    private int getReadingMinutes(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        String[] words = text.split("\\s+");
        return words.length / AVERAGE_READING_WORDS_AMOUNT;
    }

    private Post createPost(BlogPostRequestDto requestDto, Authentication authentication) {
        Post post = new Post();

        List<PostTranslations> postTranslations = new ArrayList<>();

        PostTranslations translation = createPostTranslation(
                post,
                requestDto,
                optionsService.getDefaultSiteLanguage().getLangCode());
        postTranslations.add(translation);

        post.setUser(userService.findByUsername(authentication.getName()));
        post.setCategories(requestDto.getCategoriesIds().stream()
                .map(categoryService::getBlogCategory)
                .collect(Collectors.toList()));
        post.setTranslations(postTranslations);
        post.setStatus(Post.Status.DRAFT);
        post.setReadingMinutes(getReadingMinutes(requestDto.getText()));
        post.setAlias(transliterateUtils.getAlias(Post.class.getSimpleName(), translation.getTitle()));
        return post;
    }

    private String checkedLang(String langCode) {
        return languageService.getByLangCode(langCode).getLangCode();
    }

    private List<Post> findAllByBlogCategoryId(Long categoryId, Pageable pageable) {
        return postRepository.findPostsByCategoryId(categoryId, pageable);
    }

    private List<BlogCategory> getFilteredCategories(Post post, String langCode) {
        return post.getCategories().stream()
                .peek(category -> category.setTranslations(
                        category.getTranslations().stream()
                                .filter(c -> c.getLangCode().equals(checkedLang(langCode)))
                                .collect(Collectors.toList())
                ))
                .filter(category -> !category.getTranslations().isEmpty())
                .collect(Collectors.toList());
    }

    private List<PostTranslations> getTranslation(Long postId, String langCode) {
        List<PostTranslations> translations = findById(postId).getTranslations().stream()
                .filter(t -> t.getLangCode().equals(checkedLang(langCode)))
                .collect(Collectors.toList());

        if (translations.isEmpty()) {
            translations = findById(postId).getTranslations().stream()
                    .filter(postTranslations -> postTranslations.getLangCode().equals(
                            optionsService.getDefaultSiteLanguage().getLangCode()))
                    .collect(Collectors.toList());
        }
        return translations;
    }
}
