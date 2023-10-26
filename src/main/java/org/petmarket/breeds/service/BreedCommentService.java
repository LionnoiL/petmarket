package org.petmarket.breeds.service;

import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.breeds.dto.BreedCommentRequestDto;
import org.petmarket.breeds.dto.BreedCommentResponseDto;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Stack;

public interface BreedCommentService {
    BreedCommentResponseDto addComment(Long breedId,
                                       BreedCommentRequestDto requestDto,
                                       Authentication authentication);

    BreedCommentResponseDto get(Long id);

    List<BreedCommentResponseDto> findAllCommentAdmin();

    List<BreedCommentResponseDto> updateStatus(Stack<Long> commentsIds, CommentStatus status);

    void delete(Long id);
}

