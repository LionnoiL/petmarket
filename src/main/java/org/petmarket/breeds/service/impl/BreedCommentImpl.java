package org.petmarket.breeds.service.impl;

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
import org.petmarket.security.jwt.JwtUser;
import org.petmarket.users.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BreedCommentImpl implements BreedCommentService {
    private final UserService userService;
    private final BreedService breedService;
    private final BreedCommentMapper commentMapper;
    private final BreedCommentRepository commentRepository;

    @Override
    public BreedCommentResponseDto addComment(Long breedId,
                                              BreedCommentRequestDto requestDto,
                                              Authentication authentication) {
        BreedComment breedComment = new BreedComment();

        breedComment.setUser(userService.findByUsername(authentication.getName()));
        breedComment.setBreed(breedService.findBreedById(breedId));
        breedComment.setComment(requestDto.getComment());
        breedComment.setStatus(CommentStatus.PENDING);
        return commentMapper.toDto(commentRepository.save(breedComment));
    }

    @Override
    public BreedCommentResponseDto get(Long commentId) {
        return commentMapper.toDto(commentRepository.findById(commentId).orElseThrow(
                () -> new ItemNotFoundException("Can't find comment: " + commentId)
        ));
    }

    @Override
    public List<BreedCommentResponseDto> findAllCommentAdmin(Pageable pageable, CommentStatus status) {
        List<BreedComment> comments;
        if (status != null) {
            comments = commentRepository.findByStatus(status, pageable);
        } else {
            comments = commentRepository.findAll(pageable).getContent();
        }
        return comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BreedCommentResponseDto> updateStatus(Stack<Long> commentIds, CommentStatus status) {

        List<BreedCommentResponseDto> resultList = new ArrayList<>();
        while (!commentIds.isEmpty()) {
            Long commentId = commentIds.pop();
            BreedComment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new ItemNotFoundException("Can't find comment: " + commentId)
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
    public void deleteMyBreedComment(Long commentId, Authentication authentication) {
        String commentUsername = commentRepository.findById(commentId).orElseThrow(
                () -> new ItemNotFoundException("Can't find comment by id: " + commentId)
        ).getUser().getUsername();
        String sessionUsername = ((JwtUser) ((UsernamePasswordAuthenticationToken) authentication)
                .getPrincipal()).getUsername();
        if (!commentUsername.equals(sessionUsername)) {
            throw new ItemNotUpdatedException("User is not comment owner: " + commentUsername);

        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
