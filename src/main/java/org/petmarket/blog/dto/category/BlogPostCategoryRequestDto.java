package org.petmarket.blog.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogPostCategoryRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String description;
}
