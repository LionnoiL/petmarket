package org.petmarket.blog.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogPostCommentRequest {
    @NotNull
    private String comment;
}
