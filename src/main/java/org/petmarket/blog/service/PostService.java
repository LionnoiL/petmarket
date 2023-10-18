package org.petmarket.blog.service;

import org.petmarket.blog.dto.posts.BlogPostRequestDto;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.entity.Post;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PostService extends AbstractService<BlogPostResponseDto, BlogPostRequestDto> {
    Post findById(Long id);

    BlogPostResponseDto savePost(BlogPostRequestDto requestDto,
                                 Authentication authentication);

    BlogPostResponseDto addTranslation(Long postId,
                                       String langCode,
                                       BlogPostRequestDto requestDto);

    List<BlogPostResponseDto> getAllByCategory(String langCode,
                                               Long categoryId);

    BlogPostResponseDto updateStatus(Long postId, String status);
}
