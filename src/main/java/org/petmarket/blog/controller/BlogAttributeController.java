package org.petmarket.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.attribute.BlogPostAttributeResponseDto;
import org.petmarket.blog.service.AttributeService;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/blog/attributes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Blog Attribute", description = "Blog Attribute endpoints API")
public class BlogAttributeController {
    private final AttributeService attributeService;

    @Operation(
            summary = "Get all attributes"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{langCode}")
    public List<BlogPostAttributeResponseDto> getAllAttributes(@RequestBody
                                                               @Valid
                                                               @Parameter(description = "Blog attribute request dto",
                                                                       required = true)
                                                               @ParameterLanguage @PathVariable String langCode) {
        return attributeService.getAll(Pageable.unpaged(), langCode);
    }
}
