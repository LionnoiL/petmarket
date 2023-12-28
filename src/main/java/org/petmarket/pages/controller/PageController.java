package org.petmarket.pages.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.pages.dto.SitePageResponseDto;
import org.petmarket.pages.entity.SitePageType;
import org.petmarket.pages.service.SitePageService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Pages", description = "the site pages API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/pages")
public class PageController {

    private final SitePageService pageService;

    @Operation(summary = "Get all Pages.", description = "Obtaining all site pages")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = SitePageResponseDto.class))
            )
    })
    @ApiResponseNotFound
    @GetMapping("/{langCode}")
    public ResponseEntity<Collection<SitePageResponseDto>> getAll(
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get all Pages.");
        Collection<SitePageResponseDto> dtos = pageService.getAll(langCode);
        log.info("All Pages were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get Page by ID")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}/{langCode}")
    public SitePageResponseDto getPageById(
            @ParameterId @PathVariable @Positive Long id,
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get the Page with id - {}.", id);
        SitePageResponseDto dto = pageService.findById(id, langCode);
        log.info("the Page with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get Page by TYPE")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{langCode}/type/{pageType}")
    public SitePageResponseDto getPageByType(
            @Parameter(description = "The TYPE of the page to retrieve.",
                    required = true
            )
            @PathVariable SitePageType pageType,
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get the Page with type - {}.", pageType);
        SitePageResponseDto dto = pageService.findByType(pageType, langCode);
        log.info("the Page with type - {} was retrieved - {}.", pageType, dto);
        return dto;
    }
}
