package org.petmarket.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/blog")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "Blog endpoints API")
public class BlogPostController {
    private final PostService postService;

    @Operation(
            summary = "Get a specific blog post by ID and language code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Blog post found and returned"),
                    @ApiResponse(responseCode = "404", description = "Not Found - Blog post not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            },
            parameters = {
                    @Parameter(
                            name = "postId",
                            description = "Post ID",
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
    @GetMapping("/{postId}/{langCode}")
    public BlogPostResponseDto getPost(
            @PathVariable Long postId,
            @PathVariable String langCode) {
        return postService.get(postId, langCode);
    }

    @Operation(
            summary = "Get all blog posts by language code and category (if specified)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Blog posts found and returned"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            },
            parameters = {
                    @Parameter(
                            name = "langCode",
                            description = "Language code",
                            example = "ua",
                            required = true
                    ),
                    @Parameter(
                            name = "categoryId",
                            description = "Blog post category ID",
                            example = "1",
                            required = false
                    )
            }
    )
    @GetMapping("/{langCode}")
    public List<BlogPostResponseDto> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @PathVariable String langCode,
            @RequestParam(required = false) Long categoryId) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "created");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return postService.getAllByCategory(langCode, categoryId, pageable);
    }
}
