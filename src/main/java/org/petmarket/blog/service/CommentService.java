package org.petmarket.blog.service;

import org.petmarket.blog.dto.comment.BlogPostCommentRequest;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentService extends AbstractService<BlogPostCommentResponse,
        BlogPostCommentRequest> {
    BlogPostCommentResponse addComment(Long postId,
                                       BlogPostCommentRequest blogPostCommentRequest,
                                       Authentication authentication);

    List<BlogPostCommentResponse> findAllCommentAdmin(Pageable pageable);

    BlogPostCommentResponse updateStatus(Long commentId, String status);
}
