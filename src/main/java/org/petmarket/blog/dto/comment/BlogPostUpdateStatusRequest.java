package org.petmarket.blog.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Stack;

@Data
public class BlogPostUpdateStatusRequest {
    @NotNull
    @JsonProperty("comments_ids")
    private Stack<Long> commentsIds;
    @NotNull
    private String status;
}
