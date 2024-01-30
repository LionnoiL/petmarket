package org.petmarket.blog.service;

import org.petmarket.blog.dto.posts.BlogPostRequestDto;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.dto.posts.BlogPostTranslationRequestDto;
import org.petmarket.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PostService extends AbstractService<BlogPostResponseDto, BlogPostRequestDto> {
    Post findById(Long postId);

    BlogPostResponseDto savePost(BlogPostRequestDto requestDto,
                                 Authentication authentication);

    BlogPostResponseDto addTranslation(Long postId,
                                       String langCode,
                                       BlogPostTranslationRequestDto requestDto);

    List<BlogPostResponseDto> getAllByCategory(String langCode,
                                               Long categoryId,
                                               Pageable pageable);

    BlogPostResponseDto updateStatus(Long postId, Post.Status status);

    Page<BlogPostResponseDto> search(String langCode, String query, int page, int size);
}
