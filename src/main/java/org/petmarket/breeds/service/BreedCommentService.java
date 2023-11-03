package org.petmarket.breeds.service;

import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.breeds.dto.BreedCommentRequestDto;
import org.petmarket.breeds.dto.BreedCommentResponseDto;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Stack;

public interface BreedCommentService {
    BreedCommentResponseDto addComment(Long breedId,
                                       BreedCommentRequestDto requestDto,
                                       Authentication authentication);

    BreedCommentResponseDto get(Long id);

    List<BreedCommentResponseDto> findAllCommentAdmin(Pageable pageable, CommentStatus status);

    List<BreedCommentResponseDto> updateStatus(Stack<Long> commentsIds, CommentStatus status);

    void deleteMyBreedComment(Long commentsId, Authentication authentication);

    void delete(Long commentId);
}

