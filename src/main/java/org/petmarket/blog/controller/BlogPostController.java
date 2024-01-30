package org.petmarket.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.service.PostService;
import org.petmarket.utils.annotations.parametrs.*;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/blog")
@RequiredArgsConstructor
@Validated
@Tag(name = "Blog", description = "Blog endpoints API")
public class BlogPostController {

    private final PostService postService;

    @Operation(
            summary = "Get a specific blog post by ID and language code"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{postId}/{langCode}")
    public BlogPostResponseDto getPost(
            @ParameterId @Positive @PathVariable Long postId,
            @ParameterLanguage @PathVariable String langCode) {
        return postService.get(postId, langCode);
    }

    @Operation(
            summary = "Get all blog posts by language code and category (if specified)"
    )
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = BlogPostResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{langCode}")
    public List<BlogPostResponseDto> getAll(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "12") @Positive int size,
            @ParameterPageSort @RequestParam(defaultValue = "ASC") String sortDirection,
            @ParameterLanguage @PathVariable String langCode,
            @ParameterId @RequestParam(required = false) @Positive Long categoryId) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "created");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return postService.getAllByCategory(langCode, categoryId, pageable);
    }

    @Operation(
            summary = "Search blog posts by language code and search query"
    )
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = BlogPostResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/search/{langCode}")
    public Page<BlogPostResponseDto> search(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "12") @Positive int size,
            @ParameterLanguage @PathVariable String langCode,
            @RequestParam(required = false) String searchQuery) {
        return postService.search(langCode, searchQuery, page, size);
    }

    @Operation(
            summary = "Get all blog posts by language code and attribute (if specified)"
    )
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = BlogPostResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/attribute/{langCode}")
    public List<BlogPostResponseDto> getPostsByAttributeId(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "12") @Positive int size,
            @ParameterLanguage @PathVariable String langCode,
            @ParameterId @RequestParam(required = false) @Positive Long attributeId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return postService.getPostsByAttributeId(langCode, attributeId, pageable);
    }
}
