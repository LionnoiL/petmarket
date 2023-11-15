package org.petmarket.language.controller;

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
import org.petmarket.language.dto.LanguageCreateRequestDto;
import org.petmarket.language.dto.LanguageResponseDto;
import org.petmarket.language.dto.LanguageUpdateRequestDto;
import org.petmarket.language.service.LanguageService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Languages", description = "the language API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/admin/languages")
public class LanguageAdminController {

    private final LanguageService languageService;

    @Operation(summary = "Create a new Language")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = LanguageResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = FORBIDDEN, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping
    @ResponseBody
    public LanguageResponseDto addLanguage(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final LanguageCreateRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Language - {}.", request);
        return languageService.addLanguage(request, bindingResult);
    }

    @Operation(summary = "Update Language by CODE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = LanguageResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = FORBIDDEN, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = LANGUAGE_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PutMapping("/{langCode}")
    @ResponseBody
    public LanguageResponseDto updateLanguage(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final LanguageUpdateRequestDto request,
            @Parameter(description = "The CODE of the language to update", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode, BindingResult bindingResult) {
        log.info("Received request to update Language - {} with code {}.", request, langCode);
        return languageService.updateLanguage(langCode, request, bindingResult);
    }

    @Operation(summary = "Enable Language by CODE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = FORBIDDEN, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = LANGUAGE_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PutMapping("/{langCode}/enable")
    @ResponseBody
    public LanguageResponseDto enableLanguage(
            @Parameter(description = "The language code to enable", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode) {
        log.info("Received request to enable Language with code {}.", langCode);
        return languageService.enableLanguage(langCode);
    }

    @Operation(summary = "Disable Language by CODE", description = """
            Language deactivation.
            If the language to be deactivated is specified as the default language
            in the site settings, we will get status 400.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = FORBIDDEN, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = LANGUAGE_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PutMapping("/{langCode}/disable")
    @ResponseBody
    public LanguageResponseDto disableLanguage(
            @Parameter(description = "The language code to disable", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode) {
        log.info("Received request to disable Language with code {}.", langCode);
        return languageService.disableLanguage(langCode);
    }
}
