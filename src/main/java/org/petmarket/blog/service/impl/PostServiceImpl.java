package org.petmarket.blog.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.petmarket.blog.dto.posts.BlogPostRequestDto;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.dto.posts.BlogPostTranslationRequestDto;
import org.petmarket.blog.entity.*;
import org.petmarket.blog.mapper.BlogPostMapper;
import org.petmarket.blog.repository.PostRepository;
import org.petmarket.blog.service.AttributeService;
import org.petmarket.blog.service.CategoryService;
import org.petmarket.blog.service.PostService;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.petmarket.options.service.OptionsService;
import org.petmarket.users.service.UserService;
import org.petmarket.utils.TransliterateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private final AttributeService attributeService;
    private final EntityManager entityManager;

    @Transactional
    @Override
    public BlogPostResponseDto get(Long id, String langCode) {
        Post post = findById(id);
        post.setComments(post.getComments().stream()
                .filter(comment -> comment.getStatus().equals(CommentStatus.APPROVED))
                .toList());
        return postMapper.toDto(post, checkedLang(langCode));
    }

    @Override
    public List<BlogPostResponseDto> getAll(Pageable pageable, String langCode) {

        return postRepository.findAll(pageable).stream()
                .map(post -> postMapper.toDto(post, checkedLang(langCode)))
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
            if (translation.getLanguage().getLangCode().equals(checkedLang(langCode).getLangCode())) {
                translation.setTitle(requestDto.getTitle());
                translation.setDescription(requestDto.getText());
                translation.setShortText(truncateStringTo500Characters(requestDto.getText()));
            }
        }
        post.setCategories(categoryService.getBlogCategories(requestDto.getCategoriesIds()));
        post.setAttributes(attributeService.getBlogAttributes(requestDto.getAttributesIds()));
        post.setReadingMinutes(getReadingMinutes(requestDto.getText()));
        postRepository.save(post);
        return postMapper.toDto(post, checkedLang(langCode));
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
        return postMapper.toDto(postRepository.save(post), optionsService.getDefaultSiteLanguage());
    }

    @Transactional
    @Override
    public BlogPostResponseDto addTranslation(Long postId,
                                              String langCode,
                                              BlogPostTranslationRequestDto requestDto) {
        Post post = findById(postId);
        List<PostTranslations> translations = post.getTranslations();
        if (translations.stream()
                .anyMatch(t -> t.getLanguage().getLangCode()
                        .equals(checkedLang(langCode).getLangCode()))) {
            throw new ItemNotUpdatedException(langCode + " translation already exist");
        } else {
            PostTranslations translation = PostTranslations.builder()
                    .post(post)
                    .title(requestDto.getTitle())
                    .description(requestDto.getText())
                    .language(languageService.getByLangCode(langCode))
                    .shortText(truncateStringTo500Characters(requestDto.getText()) + " ...")
                    .build();
            translations.add(translation);
            post.setTranslations(translations);
            postRepository.save(post);
        }
        return postMapper.toDto(post, checkedLang(langCode));
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
                    post.setComments(post.getComments().stream()
                            .filter(blogComment -> blogComment.getStatus().equals(CommentStatus.APPROVED))
                            .toList());
                    return post;
                })
                .map(post -> postMapper.toDto(post, checkedLang(langCode)))
                .toList();
    }

    @Transactional
    @Override
    public BlogPostResponseDto updateStatus(Long postId, Post.Status status) {
        Post post = findById(postId);
        post.setStatus(status);
        postRepository.save(post);
        return postMapper.toDto(post, optionsService.getDefaultSiteLanguage());
    }

    @Transactional
    @Override
    public BlogPostResponseDto save(BlogPostRequestDto requestDto) {
        return null;
    }

    @Override
    public Page<BlogPostResponseDto> search(String langCode, String query, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        if (query == null || query.isEmpty()) {
            List<BlogPostResponseDto> all = new ArrayList<>(getAll(pageable, langCode));
            all.sort((o1, o2) -> o2.getUpdated().compareTo(o1.getUpdated()));
            return new PageImpl<>(all, pageable, all.size());
        }

        SearchSession searchSession = Search.session(entityManager);
        List<Post> posts = searchSession.search(Post.class)
                .where(f -> f.match()
                        .fields("translations.title",
                                "translations.description",
                                "translations.shortText",
                                "categories.translations.title",
                                "categories.translations.description")
                        .matching(query).fuzzy(1))
                .sort(f -> f.field("updated").desc())
                .fetchHits((page - 1) * size, size);
        List<BlogPostResponseDto> postDtos = posts.stream()
                .map(post -> postMapper.toDto(post, checkedLang(langCode))).toList();

        return new PageImpl<>(postDtos, pageable, postDtos.size());
    }

    public List<BlogPostResponseDto> getPostsByAttributeId(String langCode, Long attributeId, Pageable pageable) {
        if (attributeId == null) {
            return getAll(pageable, langCode);
        }

        return postRepository
                .findPostsByAttributeId(attributeId, pageable)
                .stream()
                .map(post -> postMapper.toDto(post, checkedLang(langCode)))
                .toList();
    }

    private String truncateStringTo500Characters(String input) {
        if (input.length() <= 495) {
            return input;
        } else {
            return input.substring(0, 495);
        }
    }

    private PostTranslations createPostTranslation(Post post, BlogPostRequestDto requestDto, Language language) {
        return PostTranslations.builder()
                .post(post)
                .title(requestDto.getTitle())
                .description(requestDto.getText())
                .language(language)
                .shortText(truncateStringTo500Characters(requestDto.getText()) + " ...")
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
                optionsService.getDefaultSiteLanguage());
        postTranslations.add(translation);

        post.setUser(userService.findByUsername(authentication.getName()));
        post.setCategories(categoryService.getBlogCategories(requestDto.getCategoriesIds()));
        post.setAttributes(attributeService.getBlogAttributes(requestDto.getAttributesIds()));
        post.setTranslations(postTranslations);
        post.setStatus(Post.Status.DRAFT);
        post.setReadingMinutes(getReadingMinutes(requestDto.getText()));
        post.setAlias(transliterateUtils.getAlias(Post.class.getSimpleName(), translation.getTitle()));
        return post;
    }

    private Language checkedLang(String langCode) {
        return languageService.getByLangCode(langCode);
    }

    private List<Post> findAllByBlogCategoryId(Long categoryId, Pageable pageable) {
        return postRepository.findPostsByCategoryId(categoryId, pageable);
    }
}
