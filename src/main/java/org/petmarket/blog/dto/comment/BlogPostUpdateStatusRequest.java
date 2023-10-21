package org.petmarket.blog.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Stack;

@Data
public class BlogPostUpdateStatusRequest {
    @NotNull
    private Stack<Long> commentsIds;
    @NotNull
    private String status;
}
