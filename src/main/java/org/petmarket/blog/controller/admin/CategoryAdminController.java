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
@Tag(name = "Blog", description = "Blog endpoints API")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Save a new blog category",
            description = "Create a new blog category with the default language code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public BlogPostCategoryResponseDto save(@RequestBody
                                            @Valid
                                            @Parameter(description = "Blog post category request dto",
                                                    required = true)
                                            BlogPostCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @PostMapping("/{categoryId}/{langCode}")
    @Operation(summary = "Add translation to a blog category",
            description = "Add translation to an existing blog category" +
                    " with the specified language code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog category translation " +
                    "added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public BlogPostCategoryResponseDto addTranslation(@PathVariable(name = "categoryId")
                                                      @Parameter(name = "categoryId",
                                                              description = "Category ID",
                                                              example = "1",
                                                              required = true)
                                                      Long categoryId,
                                                      @PathVariable(name = "langCode")
                                                      @Parameter(name = "langCode", description = "Language code",
                                                              example = "en", required = true)
                                                      String langCode,
                                                      @RequestBody
                                                      @Valid
                                                      @Parameter(description = "Blog post category request dto",
                                                              required = true)
                                                      BlogPostCategoryRequestDto requestDto) {
        return categoryService.addTranslation(categoryId, langCode, requestDto);
    }

    @PutMapping("/{categoryId}/{langCode}")
    @Operation(summary = "Add translation to a blog category",
            description = "Add translation to an existing blog category" +
                    " with the specified language code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog category translation " +
                    "added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public BlogPostCategoryResponseDto updateCategory(@PathVariable(name = "categoryId")
                                                      @Parameter(name = "categoryId",
                                                              description = "Category ID",
                                                              example = "1",
                                                              required = true)
                                                      Long categoryId,
                                                      @PathVariable(name = "langCode")
                                                      @Parameter(name = "langCode", description = "Language code",
                                                              example = "ua", required = true)
                                                      String langCode,
                                                      @RequestBody
                                                      @Valid
                                                      @Parameter(description = "Blog post category request dto",
                                                              required = true)
                                                      BlogPostCategoryRequestDto requestDto) {
        return categoryService.updateById(categoryId, langCode, requestDto);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a blog category", description = "Delete a blog category by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Blog category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void deleteCategory(@PathVariable(name = "categoryId")
                               @Parameter(name = "categoryId",
                                       description = "Category ID",
                                       example = "1",
                                       required = true)
                               Long categoryId) {
        categoryService.delete(categoryId);
    }
}

