package org.petmarket.blog.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogPostCommentRequest {
    @NotNull
    @Schema(example = "Це дуже корисна інформація дякую ВАМ!", description = "Комментар до блог посту")
    private String comment;
}
