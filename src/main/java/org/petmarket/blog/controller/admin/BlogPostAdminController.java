package org.petmarket.blog.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.posts.BlogPostRequestDto;
import org.petmarket.blog.dto.posts.BlogPostResponseDto;
import org.petmarket.blog.dto.posts.BlogPostTranslationRequestDto;
import org.petmarket.blog.entity.Post;
import org.petmarket.blog.service.PostService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_DELETED;

@RestController
@RequestMapping("/v1/admin/blog")
@RequiredArgsConstructor
@Validated
@Tag(name = "Blog", description = "Blog endpoints API")
public class BlogPostAdminController {
    private final PostService postService;

    @Operation(
            summary = "Create a new blog post",
            description = "Create a new blog post with the default language code"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
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
            description = "Add a new translation for a blog post."
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping("/{postId}/{langCode}")
    public BlogPostResponseDto addTranslation(@RequestBody
                                              @Valid
                                              @Parameter(description = "Blog post translation request dto",
                                                      required = true)
                                              BlogPostTranslationRequestDto requestDto,
                                              @ParameterId @PathVariable @Positive Long postId,
                                              @ParameterLanguage @PathVariable String langCode) {
        return postService.addTranslation(postId, langCode, requestDto);
    }

    @Operation(
            summary = "Update post status",
            description = "Update the status of a blog post."
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PutMapping("/{postId}/status/{status}")
    public BlogPostResponseDto updateStatus(
            @ParameterId @PathVariable @Positive Long postId,
            @PathVariable Post.Status status) {
        return postService.updateStatus(postId, status);
    }

    @Operation(
            summary = "Update a post",
            description = "Update the content of a blog post."
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PutMapping("/{postId}/{langCode}")
    public BlogPostResponseDto updatePost(@RequestBody
                                          @Valid
                                          @Parameter(description = "Blog post request dto", required = true)
                                          BlogPostRequestDto requestDto,
                                          @ParameterId @PathVariable @Positive Long postId,
                                          @ParameterLanguage @PathVariable String langCode) {
        return postService.updateById(postId, langCode, requestDto);
    }

    @Operation(
            summary = "Delete a blog post",
            description = "Delete a blog post by ID"
    )
    @ApiResponse(responseCode = "204", description = SUCCESSFULLY_DELETED)
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{postId}")
    public void delete(@ParameterId @PathVariable @Positive Long postId) {
        postService.delete(postId);
    }
}
