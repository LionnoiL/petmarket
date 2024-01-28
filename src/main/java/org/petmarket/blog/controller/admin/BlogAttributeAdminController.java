package org.petmarket.blog.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.attribute.BlogPostAttributeRequestDto;
import org.petmarket.blog.dto.attribute.BlogPostAttributeResponseDto;
import org.petmarket.blog.service.AttributeService;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin/blog/attributes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Blog Attribute", description = "Blog Attribute endpoints API")
public class BlogAttributeAdminController {
    private final AttributeService attributeService;

    @Operation(
            summary = "Create a new attribute"
    )
    @ApiResponseCreated
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping
    public BlogPostAttributeResponseDto createAttribute(@RequestBody
                                                        @Valid
                                                        @Parameter(description = "Blog attribute request dto",
                                                                required = true)
                                                        BlogPostAttributeRequestDto requestDto) {
        return attributeService.save(requestDto);
    }
}
