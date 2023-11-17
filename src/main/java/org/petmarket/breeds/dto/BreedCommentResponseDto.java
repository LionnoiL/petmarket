package org.petmarket.breeds.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.petmarket.blog.entity.CommentStatus;

import java.time.LocalDateTime;

@Data
public class BreedCommentResponseDto {
    @Schema(description = "Breed comment id", example = "1")
    private Long id;
    @Schema(description = "Breed comment", example = "This is test comment")
    private String comment;
    private CommentStatus status;
    @Schema(description = "User email", example = "bob@admin.com")
    @JsonProperty("user_name")
    private String userName;
    @Schema(description = "User Name", example = "Bob")
    @JsonProperty("first_name")
    private String firstName;
    @Schema(description = "User last name", example = "Alison")
    @JsonProperty("last_name")
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @Schema(description = "Breed id", example = "1")
    @JsonProperty("breed_id")
    private Long breedId;
    @Schema(description = "User id", example = "1")
    @JsonProperty("user_id")
    private Long userId;
}
