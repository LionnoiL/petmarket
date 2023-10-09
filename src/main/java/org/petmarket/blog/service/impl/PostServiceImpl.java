package org.petmarket.blog.service.impl;

import org.petmarket.blog.dto.posts.BlogPostRequestDto;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.entity.Post;
import org.petmarket.blog.entity.PostTranslations;
import org.petmarket.blog.mapper.BlogPostMapper;
import org.petmarket.blog.mapper.CategoryMapper;
import org.petmarket.blog.repository.PostRepository;
import org.petmarket.blog.service.CategoryService;
import org.petmarket.blog.service.PostService;
import org.petmarket.users.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private static final int AVERAGE_READING_WORDS_AMOUNT = 150;
    private final PostRepository postRepository;
    private final BlogPostMapper postMapper;
    private final UserService userService;
    private final CategoryService categoryService;

    public PostServiceImpl(PostRepository postRepository,
                           BlogPostMapper postMapper,
                           UserService userService,
                           CategoryService categoryService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public BlogPostResponseDto save(BlogPostRequestDto requestDto, String langCode) {
        return null;
    }

    @Override
    public BlogPostResponseDto get(Long id, String langCode) {
        return postMapper.toDto(findById(id), langCode);
    }

    @Override
    public List<BlogPostResponseDto> getAll(Pageable pageable, String langCode) {
        return postRepository.findAll(pageable).stream()
                .map(p -> postMapper.toDto(p, langCode))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public BlogPostResponseDto updateById(Long id, String langCode, BlogPostRequestDto blogPostRequestDto) {
        return null;
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Can't find Blog Post id: " + id)
        );
    }

    @Override
    public BlogPostResponseDto savePost(BlogPostRequestDto requestDto,
                                        Authentication authentication,
                                        String langCode) {
        Post post = new Post();
        List<PostTranslations> postTranslations = new ArrayList<>();
        PostTranslations translation = createPostTranslation(post, requestDto, langCode);
        postTranslations.add(translation);
        post.setUser(userService.findByUsername("admin@email.com"));
        //post.setUser(userService.findByUsername(authentication.getName()));
        post.setCategories(requestDto.getCategoriesIds().stream()
                .map(categoryService::getBlogCategory)
                .collect(Collectors.toList()));
        post.setTranslations(postTranslations);
        post.setStatus(Post.Status.DRAFT);
        post.setCreated(LocalDateTime.now());
        post.setUpdated(LocalDateTime.now());
        post.setReadingMinutes(getReadingMinutes(requestDto.getText()));
        postRepository.save(post);
        return postMapper.toDto(post, langCode);
    }

    private String truncateStringTo500Characters(String input) {
        if (input.length() <= 500) {
            return input;
        } else {
            return input.substring(0, 500);
        }
    }

    private PostTranslations createPostTranslation(Post post, BlogPostRequestDto requestDto, String langCode) {
        PostTranslations translation = new PostTranslations();
        translation.setPost(post);
        translation.setTitle(requestDto.getTitle());
        translation.setText(requestDto.getText());
        translation.setLangCode(langCode);
        translation.setShortText(truncateStringTo500Characters(requestDto.getText()));
        return translation;
    }

    public int getReadingMinutes(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        String[] words = text.split("\\s+");
        return words.length / AVERAGE_READING_WORDS_AMOUNT;
    }
}
