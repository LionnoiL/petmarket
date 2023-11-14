package org.petmarket.blog.service.impl;

import jakarta.transaction.Transactional;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final BlogCommentMapper mapper;
    private final UserService userService;

    @Transactional
    @Override
    public BlogPostCommentResponse addComment(Long postId,
                                              BlogPostCommentRequest request,
                                              Authentication authentication) {
        BlogComment comment = new BlogComment();
        comment.setUser(userService.findByUsername(authentication.getName()));
        comment.setPost(postService.findById(postId));
        comment.setStatus(CommentStatus.PENDING);
        comment.setComment(request.getComment());
        return mapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<BlogPostCommentResponse> findAllCommentAdmin() {
        return commentRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public List<BlogPostCommentResponse> updateStatus(Stack<Long> commentIds, CommentStatus status) {

        List<BlogPostCommentResponse> resultList = new ArrayList<>();
        while (!commentIds.isEmpty()) {
            Long commentId = commentIds.pop();
            BlogComment comment = getBlogComment(commentId);

            if (comment != null) {
                comment.setStatus(status);
                commentRepository.save(comment);
                resultList.add(mapper.toDto(comment));
            }
        }

        return resultList;
    }

    @Transactional
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
        return findAllCommentAdmin();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        commentRepository.delete(getBlogComment(id));
    }

    @Transactional
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
