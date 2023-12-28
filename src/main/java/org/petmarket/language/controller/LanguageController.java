package org.petmarket.language.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.language.dto.LanguageResponseDto;
import org.petmarket.language.service.LanguageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Languages", description = "the language API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/languages")
public class LanguageController {

    private final LanguageService languageService;

    @Operation(summary = "Get enabled Languages.", description = "Retrieving only active site languages")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = LanguageResponseDto.class))
            )
    })
    @GetMapping
    public List<LanguageResponseDto> getAllEnabled() {
        log.info("Received request to get active Languages.");
        List<LanguageResponseDto> languageResponseDtos = languageService.getAllEnabled();
        log.info("All Languages were retrieved - {}.", languageResponseDtos);
        return languageResponseDtos;
    }
}
