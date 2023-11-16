package org.petmarket.blog.dto.posts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BlogPostTranslationResponseDto {
    @Schema(example = "ua", description = "Код перекладу")
    private String langCode;
    @Schema(example = "Догляд за вівчаркою", description = "Назва блог посту")
    private String title;
    @Schema(example = "Вівчарок треба мити і вичісувати", description = "Текст блог посту")
    private String description;
}
