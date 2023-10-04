package org.petmarket.blog.service.impl;

import org.petmarket.blog.dto.comment.BlogPostCommentRequest;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.repository.CommentRepository;
import org.petmarket.blog.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;

    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public BlogPostCommentResponse save(BlogPostCommentRequest blogPostCommentRequest) {
        return null;
    }

    @Override
    public BlogPostCommentResponse get(Long id) {
        return null;
    }

    @Override
    public List<BlogPostCommentResponse> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public BlogPostCommentResponse updateById(Long id,
                                              BlogPostCommentRequest blogPostCommentRequest) {
        return null;
    }
}
