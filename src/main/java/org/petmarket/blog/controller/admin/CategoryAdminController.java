package org.petmarket.blog.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.category.BlogPostCategoryRequestDto;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/blog/categories")
@RequiredArgsConstructor
@Tag(name = "Admin Blog Categories Management", description = "Endpoints for managing blog categories by admin")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping("/{langCode}")
    @Operation(summary = "Save a new blog category",
            description = "Create a new blog category with the specified language code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public BlogPostCategoryResponseDto save(@RequestBody @Valid BlogPostCategoryRequestDto requestDto,
                                            @PathVariable(name = "langCode")
                                            @Parameter(description = "Language code",
                                                    required = true) String langCode) {
        return categoryService.save(requestDto, langCode);
    }

    @PutMapping("/{id}/{langCode}")
    @Operation(summary = "Add translation to a blog category",
            description = "Add translation to an existing blog category" +
                    " with the specified language code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog category translation " +
                    "added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public BlogPostCategoryResponseDto addTranslation(@PathVariable(name = "id")
                                                      @Parameter(description = "Category ID",
                                                              required = true) Long id,
                                                      @PathVariable(name = "langCode")
                                                      @Parameter(description = "Language code",
                                                              required = true) String langCode,
                                                      @RequestBody @Valid BlogPostCategoryRequestDto requestDto) {
        return categoryService.updateById(id, langCode, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a blog category", description = "Delete a blog category by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Blog category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void deleteCategory(@PathVariable(name = "id")
                                   @Parameter(description = "Category ID",
                                           required = true) Long id) {
        categoryService.delete(id);
    }
}

