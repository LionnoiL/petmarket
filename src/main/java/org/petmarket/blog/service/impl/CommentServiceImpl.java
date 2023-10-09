package org.petmarket.blog.service.impl;

import org.petmarket.blog.dto.comment.BlogPostCommentRequest;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.entity.BlogComment;
import org.petmarket.blog.mapper.BlogCommentMapper;
import org.petmarket.blog.repository.CommentRepository;
import org.petmarket.blog.service.CommentService;
import org.petmarket.blog.service.PostService;
import org.petmarket.users.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final BlogCommentMapper mapper;
    private final UserService userService;

    public CommentServiceImpl(CommentRepository repository,
                              PostService postService,
                              BlogCommentMapper mapper,
                              UserService userService) {
        this.commentRepository = repository;
        this.postService = postService;
        this.mapper = mapper;
        this.userService = userService;
    }

    @Override
    public BlogPostCommentResponse saveNoTranslation(Long postId,
                                                     BlogPostCommentRequest request,
                                                     Authentication authentication) {
        BlogComment comment = new BlogComment();
        comment.setUser(userService.findByUsername("admin@email.com"));
        //comment.setUser(userService.findByUsername(authentication.getName()));
        comment.setPost(postService.findById(postId));
        comment.setStatus(BlogComment.Status.PENDING);
        comment.setComment(request.getComment());
        comment.setCreated(LocalDateTime.now());

        return mapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<BlogPostCommentResponse> findAllPostComment(Long postId) {
        return commentRepository.findAll().stream()
                .filter(c -> c.getPost().getId().equals(postId))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogPostCommentResponse> findAllCommentAdmin(Pageable pageable) {
        return commentRepository.findAll(pageable).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public BlogPostCommentResponse updateStatus(Long commentId, String status) {
        BlogComment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NoSuchElementException("Can;t find blog comment with id: " + commentId)
        );
        comment.setStatus(BlogComment.Status.valueOf(status));
        commentRepository.save(comment);
        return mapper.toDto(comment);
    }

    @Override
    public BlogPostCommentResponse save(BlogPostCommentRequest request, String langCode) {
        return null;
    }

    @Override
    public BlogPostCommentResponse get(Long id, String langCode) {
        return mapper.toDto(commentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Can't find comment by id: " + id)));
    }

    @Override
    public List<BlogPostCommentResponse> getAll(Pageable pageable, String langCode) {
        return commentRepository.findAll(pageable).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        BlogComment comment = commentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Can't find comment: " + id)
        );
        commentRepository.delete(comment);
    }

    @Override
    public BlogPostCommentResponse updateById(Long id, String langCode,
                                              BlogPostCommentRequest blogPostCommentRequest) {
        return null;
    }
}
