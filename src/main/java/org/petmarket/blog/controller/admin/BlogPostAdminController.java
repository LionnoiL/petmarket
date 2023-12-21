package org.petmarket.blog.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.posts.BlogPostRequestDto;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.dto.posts.BlogPostTranslationRequestDto;
import org.petmarket.blog.entity.Post;
import org.petmarket.blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.*;

@RestController
@RequestMapping("/v1/admin/blog")
@RequiredArgsConstructor
@Validated
@Tag(name = "Blog", description = "Blog endpoints API")
public class BlogPostAdminController {
    private final PostService postService;

    @Operation(
            summary = "Create a new blog post",
            description = "Create a new blog post with the default language code",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            }
    )
    @PostMapping
    public BlogPostResponseDto createPost(@RequestBody
                                          @Valid
                                          @Parameter(description = "Blog post request dto", required = true)
                                          BlogPostRequestDto requestDto,
                                          Authentication authentication) {
        return postService.savePost(requestDto, authentication);
    }

    @Operation(
            summary = "Add a translation",
            description = "Add a new translation for a blog post.",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION,
                            content = @Content(schema = @Schema(implementation = BlogPostResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND)
            },
            parameters = {
                    @Parameter(name = "postId", description = "Post ID", example = "1", required = true),
                    @Parameter(name = "langCode", description = "Language code", example = "en", required = true)
            }
    )
    @PostMapping("/{postId}/{langCode}")
    public BlogPostResponseDto addTranslation(@RequestBody
                                              @Valid
                                              @Parameter(description = "Blog post translation request dto",
                                                      required = true)
                                              BlogPostTranslationRequestDto requestDto,
                                              @PathVariable Long postId,
                                              @PathVariable String langCode) {
        return postService.addTranslation(postId, langCode, requestDto);
    }

    @Operation(
            summary = "Update post status",
            description = "Update the status of a blog post.",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION,
                            content = @Content(schema = @Schema(implementation = BlogPostResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND)
            },
            parameters = {
                    @Parameter(name = "postId", description = "Post ID", example = "1", required = true)
            }
    )
    @PutMapping("/{postId}/status/{status}")
    public BlogPostResponseDto updateStatus(
            @PathVariable Long postId,
            @PathVariable Post.Status status) {
        return postService.updateStatus(postId, status);
    }

    @Operation(
            summary = "Update a post",
            description = "Update the content of a blog post.",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION,
                            content = @Content(schema = @Schema(implementation = BlogPostResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND)
            },
            parameters = {
                    @Parameter(name = "postId", description = "Post ID", example = "1", required = true),
                    @Parameter(name = "langCode", description = "Language code", example = "ua", required = true)
            }
    )
    @PutMapping("/{postId}/{langCode}")
    public BlogPostResponseDto updatePost(@RequestBody
                                          @Valid
                                          @Parameter(description = "Blog post request dto", required = true)
                                          BlogPostRequestDto requestDto,
                                          @PathVariable Long postId,
                                          @PathVariable String langCode) {
        return postService.updateById(postId, langCode, requestDto);
    }

    @Operation(
            summary = "Delete a blog post",
            description = "Delete a blog post by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = SUCCESSFULLY_DELETED),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(name = "postId", description = "Post ID", example = "1", required = true)
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
