package org.petmarket.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.service.CategoryService;
import org.petmarket.utils.annotations.parametrs.*;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/blog/categories")
@RequiredArgsConstructor
@Validated
@Tag(name = "Blog", description = "Blog endpoints API")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Get all blog categories",
            description = "Get all blog categories for the specified language code"
    )
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = BlogPostCategoryResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{langCode}")
    public List<BlogPostCategoryResponseDto> getAll(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "12") @Positive int size,
            @ParameterPageSort @RequestParam(defaultValue = "ASC") String sortDirection,
            @ParameterLanguage @PathVariable(name = "langCode") String langCode) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return categoryService.getAll(pageable, langCode);
    }

    @Operation(
            summary = "Get a blog category by ID",
            description = "Get a blog category by its ID and language code"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{categoryId}/{langCode}")
    public BlogPostCategoryResponseDto get(
            @ParameterId @PathVariable(name = "categoryId") @Positive Long categoryId,
            @ParameterLanguage @PathVariable(name = "langCode") String langCode) {
        return categoryService.get(categoryId, langCode);
    }
}
