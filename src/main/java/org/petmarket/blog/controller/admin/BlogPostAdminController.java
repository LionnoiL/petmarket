package org.petmarket.blog.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.posts.BlogPostRequestDto;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/blog")
@RequiredArgsConstructor
@Tag(name = "Admin Blog Post Management", description = "Admin Blog Post")
public class BlogPostAdminController {
    private final PostService postService;

    @Operation(summary = "Create a new blog post",
            description = "Create a new blog post with the default language code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public BlogPostResponseDto createPost(@RequestBody @Valid BlogPostRequestDto requestDto,
                                          Authentication authentication) {
        return postService.savePost(requestDto, authentication);
    }

    @Operation(summary = "Delete a blog post", description = "Delete a blog post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Blog post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Blog post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable(name = "postId") @Parameter(description = "Post ID", required = true) Long postId) {
        postService.delete(postId);
    }
}

