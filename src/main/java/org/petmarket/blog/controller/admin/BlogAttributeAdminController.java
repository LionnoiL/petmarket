package org.petmarket.blog.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.attribute.BlogPostAttributeRequestDto;
import org.petmarket.blog.dto.attribute.BlogPostAttributeResponseDto;
import org.petmarket.blog.dto.attribute.BlogPostAttributeTranslateDto;
import org.petmarket.blog.entity.BlogAttributeTranslation;
import org.petmarket.blog.service.AttributeService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/blog/attributes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Blog Attribute", description = "Blog Attribute endpoints API")
public class BlogAttributeAdminController {
    private final AttributeService attributeService;

    @Operation(
            summary = "Get all attributes"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @GetMapping("/{langCode}")
    public List<BlogPostAttributeResponseDto> getAllAttributes(@RequestBody
                                                               @Valid
                                                               @Parameter(description = "Blog attribute request dto",
                                                                       required = true)
                                                               @ParameterLanguage @PathVariable String langCode) {
        return attributeService.getAll(Pageable.unpaged(), langCode);
    }

    @Operation(
            summary = "Create a new attribute"
    )
    @ApiResponseCreated
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping("/{langCode}")
    public BlogPostAttributeResponseDto createAttribute(@RequestBody
                                                        @Valid
                                                        @Parameter(description = "Blog attribute request dto",
                                                                required = true)
                                                        BlogPostAttributeRequestDto requestDto,
                                                        @ParameterLanguage @PathVariable String langCode) {
        return attributeService.saveAttribute(requestDto, langCode);
    }

    @Operation(
            summary = "Add translation to attribute"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping("/translation/{langCode}")
    public BlogAttributeTranslation addTranslation(@ParameterId Long id,
                                                   @RequestBody
                                                   @Valid
                                                   @Parameter(description = "Blog attribute translate dto",
                                                           required = true)
                                                   BlogPostAttributeTranslateDto translateDto,
                                                   @ParameterLanguage @PathVariable String langCode) {
        return attributeService.addTranslation(id, langCode, translateDto);
    }

    @Operation(
            summary = "Update attribute"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PutMapping("/{langCode}")
    public BlogPostAttributeResponseDto updateAttribute(@ParameterId Long id,
                                                        @RequestBody
                                                        @Valid
                                                        @Parameter(description = "Blog attribute request dto",
                                                                required = true)
                                                        BlogPostAttributeRequestDto requestDto,
                                                        @ParameterLanguage @PathVariable String langCode) {
        return attributeService.updateById(id, langCode, requestDto);
    }

    @Operation(
            summary = "Delete attribute"
    )
    @ApiResponseDeleted
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @DeleteMapping("/{id}")
    public void deleteAttribute(@PathVariable Long id) {
        attributeService.delete(id);
    }

}
