package org.petmarket.blog.service.impl;

import org.petmarket.blog.dto.posts.BlogPostRequestDto;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.repository.PostRepository;
import org.petmarket.blog.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public BlogPostResponseDto save(BlogPostRequestDto blogPostRequestDto) {
        return null;
    }

    @Override
    public BlogPostResponseDto get(Long id) {
        return null;
    }

    @Override
    public List<BlogPostResponseDto> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public BlogPostResponseDto updateById(Long id, BlogPostRequestDto blogPostRequestDto) {
        return null;
    }
}
