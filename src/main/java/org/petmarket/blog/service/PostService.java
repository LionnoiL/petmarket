package org.petmarket.blog.service;

import org.petmarket.blog.dto.posts.BlogPostRequestDto;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.entity.Post;
import org.springframework.security.core.Authentication;

public interface PostService extends AbstractService<BlogPostResponseDto, BlogPostRequestDto> {
    Post findById(Long id);

    BlogPostResponseDto savePost(BlogPostRequestDto requestDto,
                                 Authentication authentication,
                                 String langCode);
}
