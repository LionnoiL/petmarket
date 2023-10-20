package org.petmarket.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/blog")
@RequiredArgsConstructor
@Tag(name = "Blog Posts", description = "Blog Post endpoints")
public class BlogPostController {
    private final PostService postService;

    @Operation(summary = "Get a specific blog post by ID and language code")
    @GetMapping("/{postId}/{langCode}")
    public BlogPostResponseDto getPost(
            @PathVariable
            @Parameter(description = "ID of the blog post", example = "1") Long postId,
            @PathVariable
            @Parameter(description = "Language code", example = "en") String langCode) {
        return postService.get(postId, langCode);
    }

    @Operation(summary = "Get all blog posts by language code and category (if specified)")
    @GetMapping("/{langCode}")
    public List<BlogPostResponseDto> getAll(
            Pageable pageable,
            @PathVariable
            @Parameter(description = "Language code", example = "en") String langCode,
            @RequestParam(required = false)
            @Parameter(description = "ID of the category", example = "1") Long categoryId) {
        return postService.getAllByCategory(langCode, categoryId);
    }
}
