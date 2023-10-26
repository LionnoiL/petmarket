package org.petmarket.breeds.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BreedRequestDto {
    @NotNull
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;
    @NotNull
    private String description;
    private Long categoryId;
}
