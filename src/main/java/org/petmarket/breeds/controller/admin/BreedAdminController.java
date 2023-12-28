package org.petmarket.breeds.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedRequestDto;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.service.BreedService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_DELETED;

@RestController
@RequestMapping("/v1/admin/breeds")
@RequiredArgsConstructor
@Validated
@Tag(name = "Breeds", description = "API endpoints for breed administration")
public class BreedAdminController {
    private final BreedService breedService;

    @Operation(
            summary = "Save Breed",
            description = "Save a new breed."
    )
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @PostMapping
    public BreedResponseDto save(@RequestBody
                                 @Schema(description = "Breed data to be saved")
                                 @Valid
                                 @Parameter(name = "Breed Request Dto",
                                         required = true)
                                 BreedRequestDto requestDto) {
        return breedService.save(requestDto);
    }

    @Operation(
            summary = "Add Translation",
            description = "Add a translation for a breed."
    )
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @PostMapping("/{breedId}/{langCode}")
    public BreedResponseDto addTranslation(
            @ParameterId @PathVariable @Positive Long breedId,
            @ParameterLanguage @PathVariable String langCode,
            @RequestBody
            @Schema(description = "Translation data to be added")
            @Parameter(name = "Breed Request Dto",
                    required = true)
            BreedRequestDto requestDto) {
        return breedService.addTranslation(breedId, langCode, requestDto);
    }

    @Operation(
            summary = "Update a breed by ID and language code",
            description = "Updates a breed with the specified ID and language code"
    )
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @PutMapping("/{breedId}/{langCode}")
    public BreedResponseDto updateBreed(
            @ParameterId @PathVariable @Positive Long breedId,
            @ParameterLanguage @PathVariable String langCode,
            @RequestBody
            @Valid
            @Parameter(name = "Breed Request Dto",
                    required = true)
            BreedRequestDto requestDto) {
        return breedService.update(breedId, langCode, requestDto);
    }

    @Operation(
            summary = "Delete a breed",
            description = "Delete a breed by ID"
    )
    @ApiResponse(responseCode = "204", description = SUCCESSFULLY_DELETED)
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{breedId}")
    public void delete(@ParameterId @PathVariable(name = "breedId") @Positive Long breedId) {
        breedService.delete(breedId);
    }
}
