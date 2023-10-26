package org.petmarket.breeds.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BreedResponseDto {
    private Long id;
    private LocalDateTime created;
    private LocalDateTime updated;
    //private Long categoryId;
    private List<BreedTranslationResponseDto> translations;
    private List<BreedCommentResponseDto> comments;
}
