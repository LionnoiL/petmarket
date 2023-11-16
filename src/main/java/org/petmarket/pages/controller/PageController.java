package org.petmarket.pages.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.pages.dto.SitePageResponseDto;
import org.petmarket.pages.entity.SitePageType;
import org.petmarket.pages.service.SitePageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.petmarket.utils.MessageUtils.PAGE_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Pages", description = "the site pages API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/pages")
public class PageController {

    private final SitePageService pageService;

    @Operation(summary = "Get all Pages.", description = "Obtaining all site pages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(
                                    implementation = SitePageResponseDto.class))
                    )
            })
    })
    @GetMapping("/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<SitePageResponseDto>> getAll(
            @Parameter(description = "The Code Language of the pages to retrieve", required = true,
                    schema = @Schema(type = "string"), example = "ua"
            )
            @PathVariable String langCode) {
        log.info("Received request to get all Pages.");
        Collection<SitePageResponseDto> dtos = pageService.getAll(langCode);
        log.info("All Pages were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get Page by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = SitePageResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = PAGE_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{id}/{langCode}")
    @ResponseBody
    public SitePageResponseDto getPageById(
            @Parameter(description = "The ID of the page to retrieve", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the page to retrieve", required = true,
                    schema = @Schema(type = "string"), example = "ua"
            )
            @PathVariable String langCode) {
        log.info("Received request to get the Page with id - {}.", id);
        SitePageResponseDto dto = pageService.findById(id, langCode);
        log.info("the Page with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get Page by TYPE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = SitePageResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = PAGE_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{langCode}/type/{pageType}")
    @ResponseBody
    public SitePageResponseDto getPageByType(
            @Parameter(description = "The TYPE of the page to retrieve. (HOW_TO_BUY, HOW_TO_SELL, ABOUT)",
                    required = true
            )
            @PathVariable SitePageType pageType,
            @Parameter(description = "The Code Language of the page to retrieve", required = true,
                    schema = @Schema(type = "string"), example = "ua"
            )
            @PathVariable String langCode) {
        log.info("Received request to get the Page with type - {}.", pageType);
        SitePageResponseDto dto = pageService.findByType(pageType, langCode);
        log.info("the Page with type - {} was retrieved - {}.", pageType, dto);
        return dto;
    }
}
