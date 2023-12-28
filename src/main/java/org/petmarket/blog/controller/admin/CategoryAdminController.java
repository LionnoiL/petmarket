package org.petmarket.blog.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.category.BlogPostCategoryRequestDto;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.service.CategoryService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/blog/categories")
@RequiredArgsConstructor
@Validated
@Tag(name = "Blog", description = "Blog endpoints API")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Save a new blog category",
            description = "Create a new blog category with the default language code"
    )
    @ApiResponseCreated
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping
    public ResponseEntity<BlogPostCategoryResponseDto> save(@RequestBody
                                                            @Valid
                                                            @Parameter(description = "Blog post category request dto",
                                                                    required = true)
                                                            BlogPostCategoryRequestDto requestDto) {
        BlogPostCategoryResponseDto responseDto = categoryService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(
            summary = "Add translation to a blog category",
            description = "Add translation to an existing blog category with the specified language code"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping("/{categoryId}/{langCode}")
    public BlogPostCategoryResponseDto addTranslation(@ParameterId @PathVariable @Positive Long categoryId,
                                                      @ParameterLanguage @PathVariable String langCode,
                                                      @RequestBody
                                                      @Valid
                                                      @Parameter(description = "Blog post category request dto",
                                                              required = true)
                                                      BlogPostCategoryRequestDto requestDto) {
        return categoryService.addTranslation(categoryId, langCode, requestDto);
    }

    @Operation(
            summary = "Edit blog category",
            description = "Edit translation to an existing blog category with the specified language code"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PutMapping("/{categoryId}/{langCode}")
    public BlogPostCategoryResponseDto updateCategory(@ParameterId @PathVariable @Positive Long categoryId,
                                                      @ParameterLanguage @PathVariable String langCode,
                                                      @RequestBody
                                                      @Valid
                                                      @Parameter(description = "Blog post category request dto",
                                                              required = true)
                                                      BlogPostCategoryRequestDto requestDto) {
        return categoryService.updateById(categoryId, langCode, requestDto);
    }

    @Operation(
            summary = "Delete a blog category",
            description = "Delete a blog category by ID"
    )
    @ApiResponseDeleted
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@ParameterId @PathVariable @Positive Long categoryId) {
        categoryService.delete(categoryId);
    }
}
