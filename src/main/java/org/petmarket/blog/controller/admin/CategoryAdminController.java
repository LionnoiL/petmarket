package org.petmarket.blog.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.category.BlogPostCategoryRequestDto;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.*;

@RestController
@RequestMapping("/v1/admin/blog/categories")
@RequiredArgsConstructor
@Validated
@Tag(name = "Blog", description = "Blog endpoints API")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Save a new blog category",
            description = "Create a new blog category with the default language code",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            }
    )
    @PostMapping
    public BlogPostCategoryResponseDto save(@RequestBody
                                            @Valid
                                            @Parameter(description = "Blog post category request dto",
                                                    required = true)
                                            BlogPostCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @Operation(
            summary = "Add translation to a blog category",
            description = "Add translation to an existing blog category with the specified language code",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(name = "categoryId", description = "Category ID", example = "1", required = true),
                    @Parameter(name = "langCode", description = "Language code", example = "en", required = true)
            }
    )
    @PostMapping("/{categoryId}/{langCode}")
    public BlogPostCategoryResponseDto addTranslation(@PathVariable Long categoryId,
                                                      @PathVariable String langCode,
                                                      @RequestBody
                                                      @Valid
                                                      @Parameter(description = "Blog post category request dto",
                                                              required = true)
                                                      BlogPostCategoryRequestDto requestDto) {
        return categoryService.addTranslation(categoryId, langCode, requestDto);
    }

    @Operation(
            summary = "Edit blog category",
            description = "Edit translation to an existing blog category with the specified language code",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(name = "categoryId", description = "Category ID", example = "1", required = true),
                    @Parameter(name = "langCode", description = "Language code", example = "ua", required = true)
            }
    )
    @PutMapping("/{categoryId}/{langCode}")
    public BlogPostCategoryResponseDto updateCategory(@PathVariable Long categoryId,
                                                      @PathVariable String langCode,
                                                      @RequestBody
                                                      @Valid
                                                      @Parameter(description = "Blog post category request dto",
                                                              required = true)
                                                      BlogPostCategoryRequestDto requestDto) {
        return categoryService.updateById(categoryId, langCode, requestDto);
    }

    @Operation(
            summary = "Delete a blog category",
            description = "Delete a blog category by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = SUCCESSFULLY_DELETED),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(name = "categoryId", description = "Category ID", example = "1", required = true)
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
    }
}
