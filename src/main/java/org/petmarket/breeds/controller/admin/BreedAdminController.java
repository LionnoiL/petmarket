package org.petmarket.breeds.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedRequestDto;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.service.BreedService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.*;

@RestController
@RequestMapping("/v1/admin/breeds")
@RequiredArgsConstructor
@Validated
@Tag(name = "Breeds", description = "API endpoints for breed administration")
public class BreedAdminController {
    private final BreedService breedService;

    @Operation(
            summary = "Save Breed",
            description = "Save a new breed.",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            }
    )
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
            description = "Add a translation for a breed.",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(name = "breedId", description = "Breed Id", example = "1"),
                    @Parameter(name = "langCode", description = "Language code for translation", example = "en")
            }
    )
    @PostMapping("/{breedId}/{langCode}")
    public BreedResponseDto addTranslation(
            @PathVariable Long breedId,
            @PathVariable String langCode,
            @RequestBody
            @Schema(description = "Translation data to be added")
            @Parameter(name = "Breed Request Dto",
                    required = true)
            BreedRequestDto requestDto) {
        return breedService.addTranslation(breedId, langCode, requestDto);
    }

    @Operation(
            summary = "Update a breed by ID and language code",
            description = "Updates a breed with the specified ID and language code",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(name = "breedId", example = "1", required = true),
                    @Parameter(name = "langCode", example = "ua", required = true),
            }
    )
    @PutMapping("/{breedId}/{langCode}")
    public BreedResponseDto updateBreed(
            @PathVariable Long breedId,
            @PathVariable String langCode,
            @RequestBody
            @Valid
            @Parameter(name = "Breed Request Dto",
                    required = true)
            BreedRequestDto requestDto) {
        return breedService.update(breedId, langCode, requestDto);
    }

    @Operation(
            summary = "Delete a breed",
            description = "Delete a breed by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = SUCCESSFULLY_DELETED),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(name = "breedId", description = "Breed Id", example = "1", required = true)
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{breedId}")
    public void delete(@PathVariable(name = "breedId") Long breedId) {
        breedService.delete(breedId);
    }

}
