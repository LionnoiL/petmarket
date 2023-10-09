package org.petmarket.blog.dto.comment;

import lombok.Data;
import org.petmarket.blog.entity.BlogComment;
import java.time.LocalDateTime;

@Data
public class BlogPostCommentResponse {
    private Long id;
    private LocalDateTime created;
    private String userName;
    private String comment;
    private BlogComment.Status status;
}
