package org.petmarket.blog.service;

import org.petmarket.blog.dto.comment.BlogPostCommentRequest;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.entity.CommentStatus;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.Stack;

public interface CommentService extends AbstractService<BlogPostCommentResponse,
        BlogPostCommentRequest> {
    BlogPostCommentResponse addComment(Long postId,
                                       BlogPostCommentRequest blogPostCommentRequest,
                                       Authentication authentication);

    List<BlogPostCommentResponse> findAllCommentAdmin();

    List<BlogPostCommentResponse> updateStatus(Stack<Long> commentsIds, CommentStatus status);
}
