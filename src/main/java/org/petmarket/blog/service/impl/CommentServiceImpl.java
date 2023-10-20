package org.petmarket.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.comment.BlogPostCommentRequest;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.entity.BlogComment;
import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.blog.mapper.BlogCommentMapper;
import org.petmarket.blog.repository.CommentRepository;
import org.petmarket.blog.service.CommentService;
import org.petmarket.blog.service.PostService;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.users.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final BlogCommentMapper mapper;
    private final UserService userService;

    @Override
    public BlogPostCommentResponse addComment(Long postId,
                                                     BlogPostCommentRequest request,
                                                     Authentication authentication) {
        BlogComment comment = new BlogComment();
        comment.setUser(userService.findByUsername(authentication.getName()));
        comment.setPost(postService.findById(postId));
        comment.setStatus(CommentStatus.PENDING);
        comment.setComment(request.getComment());
        comment.setCreated(LocalDateTime.now());
        return mapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<BlogPostCommentResponse> findAllCommentAdmin(Pageable pageable) {
        return commentRepository.findAll(pageable).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public BlogPostCommentResponse updateStatus(Long commentId, String status) {
        BlogComment comment = getBlogComment(commentId);
        comment.setStatus(CommentStatus.valueOf(status));
        commentRepository.save(comment);
        return mapper.toDto(comment);
    }

    @Override
    public BlogPostCommentResponse save(BlogPostCommentRequest request) {
        return null;
    }

    @Override
    public BlogPostCommentResponse get(Long id, String langCode) {
        return mapper.toDto(getBlogComment(id));
    }

    @Override
    public List<BlogPostCommentResponse> getAll(Pageable pageable, String langCode) {
        return findAllCommentAdmin(pageable);
    }

    @Override
    public void delete(Long id) {
        commentRepository.delete(getBlogComment(id));
    }

    @Override
    public BlogPostCommentResponse updateById(Long id, String langCode,
                                              BlogPostCommentRequest blogPostCommentRequest) {
        return null;
    }

    private BlogComment getBlogComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new ItemNotFoundException("Can't find comment: " + commentId)
        );
    }
}
