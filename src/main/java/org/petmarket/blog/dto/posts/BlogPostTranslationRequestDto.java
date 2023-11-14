package org.petmarket.blog.dto.posts;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlogPostTranslationRequestDto {
    @NotNull
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Schema(example = "Keeping an eye on the shepherd", description = "Переклада назви блог посту")
    private String title;
    @NotNull
    @Schema(example = "Dogs need to be taken care of and treated", description = "Переклада тексту блог посту")
    private String text;
}
