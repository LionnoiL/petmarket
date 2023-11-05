package org.petmarket.breeds.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.petmarket.blog.entity.CommentStatus;

import java.time.LocalDateTime;

@Data
public class BreedCommentResponseDto {
    @Schema (description = "Breed comment id", example = "1")
    private Long id;
    private LocalDateTime created;
    @Schema (description = "Breed id", example = "1")
    private Long breedId;
    @Schema (description = "User id", example = "1")
    private Long userId;
    @Schema (description = "User email", example = "bob@admin.com")
    private String userName;
    @Schema (description = "User Name", example = "Bob")
    private String firstName;
    @Schema (description = "User last name", example = "Alison")
    private String lastName;
    @Schema (description = "Breed comment", example = "This is test comment")
    private String comment;
    private CommentStatus status;
}
