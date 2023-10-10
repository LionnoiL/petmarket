package org.petmarket.language.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.language.dto.LanguageResponseDto;
import org.petmarket.language.service.LanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@Tag(name = "Languages", description = "the language API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/languages")
public class LanguageController {

    private final LanguageService languageService;

    @Operation(summary = "Get all Languages.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = LanguageResponseDto.class))
                    )
            })
    })
    @GetMapping
    @ResponseBody
    public ResponseEntity<Collection<LanguageResponseDto>> getAll() {
        log.info("Received request to get all Languages.");
        Collection<LanguageResponseDto> languageResponseDtos = languageService.getAll();
        if (languageResponseDtos.isEmpty()) {
            log.info("Languages are absent.");
            return ResponseEntity.ok().body(Collections.emptyList());
        }
        log.info("All Languages were retrieved - {}.", languageResponseDtos);
        return ResponseEntity.ok().body(languageResponseDtos);
    }
}
