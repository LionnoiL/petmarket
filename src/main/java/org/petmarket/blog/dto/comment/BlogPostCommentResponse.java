package org.petmarket.blog.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.petmarket.blog.entity.CommentStatus;

import java.time.LocalDateTime;

@Data
public class BlogPostCommentResponse {
    @Schema(example = "1", description = "ID комменту")
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @Schema(example = "Це дуже корисна інформація дякую ВАМ!", description = "Комментар до блог посту")
    private String comment;
    private CommentStatus status;
    @Schema(example = "1", description = "ID посту")
    @JsonProperty("post_id")
    private Long postId;
    @Schema(example = "1", description = "ID користувача")
    @JsonProperty("user_id")
    private Long userId;
    @Schema(example = "admin@admin.com", description = "email користувача")
    @JsonProperty("user_name")
    private String userName;
    @Schema(example = "Bob", description = "Iм'я користувача")
    @JsonProperty("first_name")
    private String firstName;
    @Schema(example = "Alison", description = "Прізвище користувача")
    @JsonProperty("last_name")
    private String lastName;
}
