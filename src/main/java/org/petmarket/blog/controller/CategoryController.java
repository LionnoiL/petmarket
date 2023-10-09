package org.petmarket.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/blog/categories")
@RequiredArgsConstructor
@Tag(name = "Blog Categories Management", description = "Endpoints for managing blog categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{langCode}")
    @Operation(summary = "Get all blog categories",
            description = "Get all blog categories for the specified language code")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "List of blog categories returned successfully"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    public List<BlogPostCategoryResponseDto> getAll(@PathVariable(name = "langCode")
                                                    @Parameter(description = "Language code",
                                                            required = true) String langCode,
                                                    Pageable pageable) {
        return categoryService.getAll(pageable, langCode);
    }

    @GetMapping("/{id}/{langCode}")
    @Operation(summary = "Get a blog category by ID", description = "Get a blog category by its ID and language code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public BlogPostCategoryResponseDto get(
            @PathVariable(name = "id")
            @Parameter(description = "Category ID", required = true) Long id,
            @PathVariable(name = "langCode")
            @Parameter(description = "Language code", required = true) String langCode) {
        return categoryService.get(id, langCode);
    }
}
