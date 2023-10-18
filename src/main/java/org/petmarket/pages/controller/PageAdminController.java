package org.petmarket.pages.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.pages.dto.SitePageCreateRequestDto;
import org.petmarket.pages.dto.SitePageResponseDto;
import org.petmarket.pages.dto.SitePageUpdateRequestDto;
import org.petmarket.pages.service.SitePageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pages", description = "the site pages API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/admin/pages")
public class PageAdminController {

    private final SitePageService pageService;

    @Operation(summary = "Create a new Page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SitePageResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "The Page has already been added " +
                    "or some data is missing", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping
    @ResponseBody
    public SitePageResponseDto addPage(
            @RequestBody @Valid @NotNull(message = "Request body is mandatory") final SitePageCreateRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Page - {}.", request);
        return pageService.addPage(request, bindingResult);
    }

    @Operation(summary = "Update Page by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SitePageResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Some data is missing", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Language not found", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PutMapping("/{id}/{langCode}")
    @ResponseBody
    public SitePageResponseDto updatePage(
            @Parameter(description = "The ID of the page to update", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the page to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode,
            @RequestBody @Valid @NotNull(message = "Request body is mandatory") final SitePageUpdateRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to update Page - {} with id {}.", request, id);
        return pageService.updatePage(id, langCode, request, bindingResult);
    }

    @Operation(summary = "Delete Page by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Page not found", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletePage(
            @Parameter(description = "The ID of the page to delete", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to delete the Page with id - {}.", id);
        pageService.deletePage(id);
        log.info("the Page with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
