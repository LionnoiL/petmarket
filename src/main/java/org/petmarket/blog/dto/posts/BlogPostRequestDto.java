package org.petmarket.blog.dto.posts;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @NotNull
    @Schema(example = "https://s3-test-3433.s3.eu-north-1.amazonaws.com/advertisements/media-storage/627_L3W60_b.webp",
            description = "Link to preview image")
    @JsonProperty("preview_url")
    private String previewUrl;
}
