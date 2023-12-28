package org.petmarket.language.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.language.dto.LanguageCreateRequestDto;
import org.petmarket.language.dto.LanguageResponseDto;
import org.petmarket.language.dto.LanguageUpdateRequestDto;
import org.petmarket.language.service.LanguageService;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;
import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Languages", description = "the language API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin/languages")
public class LanguageAdminController {

    private final LanguageService languageService;

    @Operation(summary = "Get all Languages.", description = "Obtaining all site languages, including inactive ones.")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = LanguageResponseDto.class))
            )
    })
    @GetMapping
    public List<LanguageResponseDto> getAll() {
        log.info("Received request to get all Languages.");
        List<LanguageResponseDto> languageResponseDtos = languageService.getAll();
        log.info("All Languages were retrieved - {}.", languageResponseDtos);
        return languageResponseDtos;
    }

    @Operation(summary = "Create a new Language")
    @ApiResponseCreated
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @PostMapping
    public ResponseEntity<LanguageResponseDto> addLanguage(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final LanguageCreateRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Language - {}.", request);
        LanguageResponseDto responseDto = languageService.addLanguage(request, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Update Language by CODE")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @PutMapping("/{langCode}")
    public LanguageResponseDto updateLanguage(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final LanguageUpdateRequestDto request,
            @ParameterLanguage @PathVariable String langCode, BindingResult bindingResult) {
        log.info("Received request to update Language - {} with code {}.", request, langCode);
        return languageService.updateLanguage(langCode, request, bindingResult);
    }

    @Operation(summary = "Enable Language by CODE")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @PutMapping("/{langCode}/enable")
    public LanguageResponseDto enableLanguage(
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to enable Language with code {}.", langCode);
        return languageService.enableLanguage(langCode);
    }

    @Operation(summary = "Disable Language by CODE", description = """
            Language deactivation.
            If the language to be deactivated is specified as the default language
            in the site settings, we will get status 400.
            """)
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @PutMapping("/{langCode}/disable")
    public LanguageResponseDto disableLanguage(
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to disable Language with code {}.", langCode);
        return languageService.disableLanguage(langCode);
    }
}
