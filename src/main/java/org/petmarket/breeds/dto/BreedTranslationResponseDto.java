package org.petmarket.breeds.dto;

import lombok.Data;

@Data
public class BreedTranslationResponseDto {
    private String langCode;
    private String title;
    private String description;
}
