package org.petmarket.blog.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.petmarket.blog.entity.CommentStatus;

import java.time.LocalDateTime;

@Data
public class BlogPostCommentResponse {
    @Schema(example = "1", description = "ID комменту")
    private Long id;
    private LocalDateTime created;
    @Schema(example = "1", description = "ID посту")
    private Long postId;
    @Schema(example = "1", description = "ID користувача")
    private Long userId;
    @Schema(example = "admin@admin.com", description = "email користувача")
    private String userName;
    @Schema(example = "Bob", description = "Iм'я користувача")
    private String firstName;
    @Schema(example = "Alison", description = "Прізвище користувача")
    private String lastName;
    @Schema(example = "Це дуже корисна інформація дякую ВАМ!", description = "Комментар до блог посту")
    private String comment;
    private CommentStatus status;
}
