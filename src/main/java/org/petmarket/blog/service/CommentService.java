package org.petmarket.blog.service;

import org.petmarket.blog.dto.comment.BlogPostCommentRequest;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;

public interface CommentService extends AbstractService<BlogPostCommentResponse,
        BlogPostCommentRequest> {
}
