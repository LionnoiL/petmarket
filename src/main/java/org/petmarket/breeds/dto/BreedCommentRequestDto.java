package org.petmarket.breeds.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BreedCommentRequestDto {
    @NotNull
    private String comment;
}
