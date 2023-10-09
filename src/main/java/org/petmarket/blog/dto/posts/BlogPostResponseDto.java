package org.petmarket.blog.dto.posts;

import lombok.Data;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.entity.BlogComment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BlogPostResponseDto {
    private Long id;
    private String userName;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String title;
    private String text;
    private int readingMinutes;
    private List<BlogPostCategoryResponseDto> categories;
    private List<BlogComment> comments = new ArrayList<>();

}
