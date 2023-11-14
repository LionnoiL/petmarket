package org.petmarket.breeds.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.breeds.dto.BreedCommentRequestDto;
import org.petmarket.breeds.dto.BreedCommentResponseDto;
import org.petmarket.breeds.entity.BreedComment;
import org.petmarket.breeds.mapper.BreedCommentMapper;
import org.petmarket.breeds.repository.BreedCommentRepository;
import org.petmarket.breeds.service.BreedCommentService;
import org.petmarket.breeds.service.BreedService;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.users.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
@RequiredArgsConstructor
public class BreedCommentServiceImpl implements BreedCommentService {
    private static final String EXCEPTION_TEXT = "Can't find comment by ID: ";
    private final UserService userService;
    private final BreedService breedService;
    private final BreedCommentMapper commentMapper;
    private final BreedCommentRepository commentRepository;

    @Override
    @Transactional
    public BreedCommentResponseDto addComment(Long breedId,
                                              BreedCommentRequestDto requestDto,
                                              Authentication authentication) {
        BreedComment breedComment = BreedComment.builder()
                .user(userService.findByUsername(authentication.getName()))
                .breed(breedService.findBreedById(breedId))
                .comment(requestDto.getComment())
                .status(CommentStatus.PENDING)
                .build();
        return commentMapper.toDto(commentRepository.save(breedComment));
    }

    @Override
    public BreedCommentResponseDto get(Long commentId) {
        return commentMapper.toDto(commentRepository.findById(commentId).orElseThrow(
                () -> new ItemNotFoundException(EXCEPTION_TEXT + commentId)
        ));
    }

    @Override
    public List<BreedCommentResponseDto> findAllCommentAdmin(Pageable pageable, CommentStatus status) {
        List<BreedComment> comments = (status != null) ?
                commentRepository.findByStatus(status, pageable)
                : commentRepository.findAll(pageable).getContent();
        return comments.stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public List<BreedCommentResponseDto> findAll(Long breedId,
                                                 Pageable pageable,
                                                 Authentication authentication) {
        List<BreedCommentResponseDto> breedComments = new ArrayList<>(
                commentRepository.findAllByStatusAndBreedId(
                                breedId,
                                CommentStatus.APPROVED,
                                pageable).stream()
                        .map(commentMapper::toDto)
                        .toList());
        if (authentication != null) {
            breedComments.addAll(commentRepository.findAllByUsernameAndBreedId(
                            authentication.getName(),
                            breedId, CommentStatus.PENDING).stream()
                    .map(commentMapper::toDto)
                    .toList());
        }
        return breedComments;
    }

    @Override
    @Transactional
    public List<BreedCommentResponseDto> updateStatus(Stack<Long> commentIds, CommentStatus status) {

        List<BreedCommentResponseDto> resultList = new ArrayList<>();
        while (!commentIds.isEmpty()) {
            Long commentId = commentIds.pop();
            BreedComment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new ItemNotFoundException(EXCEPTION_TEXT + commentId)
            );

            if (comment != null) {
                comment.setStatus(status);
                commentRepository.save(comment);
                resultList.add(commentMapper.toDto(comment));
            }
        }
        return resultList;
    }

    @Override
    @Transactional
    public void deleteMyBreedComment(Long commentId, Authentication authentication) {
        String commentUsername = commentRepository.findById(commentId).orElseThrow(
                () -> new ItemNotFoundException(EXCEPTION_TEXT + commentId)
        ).getUser().getUsername();
        String sessionUsername = authentication.getName();
        if (!commentUsername.equals(sessionUsername)) {
            throw new ItemNotUpdatedException("User is not comment owner: " + commentUsername);

        }
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
