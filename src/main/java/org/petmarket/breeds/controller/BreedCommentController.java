package org.petmarket.breeds.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedCommentRequestDto;
import org.petmarket.breeds.dto.BreedCommentResponseDto;
import org.petmarket.breeds.service.BreedCommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/breeds/comments")
public class BreedCommentController {
    private final BreedCommentService commentService;

    @PostMapping("/{breedId}")
    public BreedCommentResponseDto addComment(@PathVariable Long breedId,
                                              @RequestBody @Valid BreedCommentRequestDto requestDto,
                                              Authentication authentication) {
        return commentService.addComment(breedId, requestDto, authentication);
    }
}
