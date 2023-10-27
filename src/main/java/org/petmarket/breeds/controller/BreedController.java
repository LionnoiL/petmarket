package org.petmarket.breeds.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.service.BreedService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/breeds")
@RequiredArgsConstructor
@Tag(name = "Breeds", description = "API endpoints for breed information")
public class BreedController {
    private final BreedService breedService;

    @Operation(summary = "Get Breed", description = "Get information about a breed.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved breed")
    @ApiResponse(responseCode = "404", description = "Breed not found")
    @GetMapping("/{breedId}/{langCode}")
    public BreedResponseDto get(@PathVariable
                                @Schema(description = "ID of the breed") Long breedId,
                                @PathVariable
                                @Schema(description = "Language code", example = "en") String langCode) {
        return breedService.get(breedId, langCode);
    }

    @Operation(summary = "Get All Breeds", description = "Get a list of all breeds.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved breeds")
    @GetMapping("/{langCode}")
    public List<BreedResponseDto> getAll(
            @PathVariable
            @Schema(description = "Language code", example = "en") String langCode,
            @RequestParam(required = false)
            @Schema(description = "ID of the category", example = "1") Long categoryId) {
        return breedService.getAllByCategory(langCode, categoryId);
    }
}
