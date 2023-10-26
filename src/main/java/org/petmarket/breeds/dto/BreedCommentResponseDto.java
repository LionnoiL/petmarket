package org.petmarket.breeds.dto;

import lombok.Data;
import org.petmarket.blog.entity.CommentStatus;

import java.time.LocalDateTime;

@Data
public class BreedCommentResponseDto {
    private Long id;
    private LocalDateTime created;
    private Long breedId;
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String comment;
    private CommentStatus status;
}
