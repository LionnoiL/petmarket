package org.petmarket.blog.dto.posts;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class BlogPostRequestDto {
    @NotNull
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Schema(example = "Догляд за вівчаркою", description = "Назва блог посту")
    private String title;
    @NotNull
    @Schema(example = "Вівчарок треба мити і вичісувати", description = "Текст блог посту")
    private String text;
    @Schema(example = "[1]", description = "List of blog posts related categories")
    private List<Long> categoriesIds;
    @Schema(example = "[1]", description = "List of blog posts related attributes")
    private List<Long> attributesIds;
}
