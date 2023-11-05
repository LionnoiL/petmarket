package org.petmarket.blog.dto.posts;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.dto.comment.BlogPostCommentResponse;
import org.petmarket.blog.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogPostResponseDto {
    @Schema(example = "1", description = "Post Id")
    private Long id;
    @Schema(example = "admin@admin.com", description = "User email")
    private String userName;
    @Schema(example = "Bob", description = "User Name")
    private String firstName;
    @Schema(example = "Alison", description = "User last name")
    private String lastName;
    private LocalDateTime created;
    private LocalDateTime updated;
    @Schema(example = "3", description = "Time to read")
    private int readingMinutes;
    private Post.Status status;
    @Schema(example = "/dogliad-za-vivcharkou", description = "Код перекладу")
    private String alias;
    private List<BlogPostCategoryResponseDto> categories;
    private List<BlogPostCommentResponse> comments;
    private List<BlogPostTranslationResponseDto> translations;

}
