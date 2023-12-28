package org.petmarket.pages.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.pages.dto.SitePageCreateRequestDto;
import org.petmarket.pages.dto.SitePageResponseDto;
import org.petmarket.pages.dto.SitePageUpdateRequestDto;
import org.petmarket.pages.service.SitePageService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;

@Tag(name = "Pages", description = "the site pages API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin/pages")
public class PageAdminController {

    private final SitePageService pageService;

    @Operation(summary = "Create a new Page")
    @ApiResponseCreated
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping
    public ResponseEntity<SitePageResponseDto> addPage(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final SitePageCreateRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Page - {}.", request);
        SitePageResponseDto responseDto = pageService.addPage(request, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Update Page by ID")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PutMapping("/{id}/{langCode}")
    public SitePageResponseDto updatePage(
            @ParameterId @PathVariable @Positive Long id,
            @ParameterLanguage @PathVariable String langCode,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final SitePageUpdateRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to update Page - {} with id {}.", request, id);
        return pageService.updatePage(id, langCode, request, bindingResult);
    }

    @Operation(summary = "Delete Page by ID")
    @ApiResponseDeleted
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePage(
            @ParameterId @PathVariable Long id) {
        log.info("Received request to delete the Page with id - {}.", id);
        pageService.deletePage(id);
        log.info("the Page with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
