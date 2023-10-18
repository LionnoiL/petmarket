package org.petmarket.blog.dto.posts;

import lombok.Data;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogPostResponseDto {
    private Long id;
    private String userName;
    private LocalDateTime created;
    private LocalDateTime updated;
    private int readingMinutes;
    private Post.Status status;
    private List<BlogPostCategoryResponseDto> categories;
    private List<BlogPostCommentResponse> comments;
    private List<BlogPostTranslationResponseDto> translations;

}
