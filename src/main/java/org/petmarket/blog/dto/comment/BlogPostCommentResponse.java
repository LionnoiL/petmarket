package org.petmarket.blog.dto.comment;

import lombok.Data;
import org.petmarket.blog.entity.CommentStatus;
import java.time.LocalDateTime;

@Data
public class BlogPostCommentResponse {
    private Long id;
    private LocalDateTime created;
    private Long postId;
    private String userName;
    private String comment;
    private CommentStatus status;
}
