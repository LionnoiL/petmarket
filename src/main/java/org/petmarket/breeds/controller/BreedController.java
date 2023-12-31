package org.petmarket.breeds.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.service.BreedService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/breeds")
@RequiredArgsConstructor
@Validated
@Tag(name = "Breeds", description = "API endpoints for breed administration")
public class BreedController {
    private final BreedService breedService;

    @Operation(
            summary = "Get Breed",
            description = "Get information about a breed."
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{breedId}/{langCode}")
    public BreedResponseDto get(@ParameterId @PathVariable @Positive Long breedId,
                                @ParameterLanguage @PathVariable String langCode) {
        return breedService.get(breedId, langCode);
    }

    @Operation(
            summary = "Get All Breeds",
            description = "Get a list of all breeds."
    )
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = BreedResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{langCode}")
    public List<BreedResponseDto> getAll(
            @ParameterLanguage @PathVariable String langCode,
            @ParameterId @RequestParam(required = false) @Positive Long categoryId) {
        return breedService.getAllByCategory(langCode, categoryId);
    }
}
