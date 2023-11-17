package org.petmarket.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.service.CategoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.*;

@RestController
@RequestMapping("/v1/blog/categories")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "Blog endpoints API")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "Get all blog categories",
            description = "Get all blog categories for the specified language code",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(
                            name = "langCode",
                            description = "Language code",
                            example = "ua",
                            required = true
                    ),
                    @Parameter(name = "page", description = "Page number", example = "1"),
                    @Parameter(name = "size", description = "Number of items per page", example = "12"),
                    @Parameter(name = "sortDirection", description = "Sort direction", example = "ASC")
            }
    )
    @GetMapping("/{langCode}")
    public List<BlogPostCategoryResponseDto> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @PathVariable(name = "langCode") String langCode) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return categoryService.getAll(pageable, langCode);
    }

    @Operation(
            summary = "Get a blog category by ID",
            description = "Get a blog category by its ID and language code",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(
                            name = "categoryId",
                            description = "Category Id",
                            example = "1",
                            required = true
                    ),
                    @Parameter(
                            name = "langCode",
                            description = "Language code",
                            example = "ua",
                            required = true
                    )
            }
    )
    @GetMapping("/{categoryId}/{langCode}")
    public BlogPostCategoryResponseDto get(
            @PathVariable(name = "categoryId") Long categoryId,
            @PathVariable(name = "langCode") String langCode) {
        return categoryService.get(categoryId, langCode);
    }
}
