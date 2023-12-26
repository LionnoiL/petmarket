package org.petmarket.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.service.PostService;
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
    @ApiResponseSuccessful
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
}
