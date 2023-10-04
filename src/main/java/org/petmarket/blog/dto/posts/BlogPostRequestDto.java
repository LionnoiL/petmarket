package org.petmarket.blog.dto.posts;

import lombok.Data;
import org.petmarket.blog.entity.Category;
import java.util.List;

@Data
public class BlogPostRequestDto {
    private String title;
    private String text;
    private List<Category> categories;
}
