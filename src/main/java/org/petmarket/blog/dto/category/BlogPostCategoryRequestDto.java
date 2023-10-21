package org.petmarket.blog.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlogPostCategoryRequestDto {
    @NotNull
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;
    @NotNull
    @Size(min = 1, max = 500, message = "Title must be between 1 and 255 characters")
    private String description;
}
