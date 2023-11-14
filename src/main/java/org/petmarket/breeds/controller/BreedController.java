package org.petmarket.breeds.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Breeds", description = "API endpoints for breed administration")
public class BreedController {
    private final BreedService breedService;

    @Operation(
            summary = "Get Breed",
            description = "Get information about a breed.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved breed"),
                    @ApiResponse(responseCode = "404", description = "Breed not found")
            },
            parameters = {
                    @Parameter(name = "breedId", description = "breed id", example = "1"),
                    @Parameter(name = "langCode", description = "Code of language", example = "ua")
            }
    )
    @GetMapping("/{breedId}/{langCode}")
    public BreedResponseDto get(@PathVariable Long breedId, @PathVariable String langCode) {
        return breedService.get(breedId, langCode);
    }

    @Operation(
            summary = "Get All Breeds",
            description = "Get a list of all breeds.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved breeds"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            },
            parameters = {
                    @Parameter(name = "langCode", description = "Code of language", example = "ua"),
                    @Parameter(name = "categoryId", description = "find breeds by category", example = "1",
                            required = false)
            }
    )
    @GetMapping("/{langCode}")
    public List<BreedResponseDto> getAll(
            @PathVariable String langCode,
            @RequestParam(required = false) Long categoryId) {
        return breedService.getAllByCategory(langCode, categoryId);
    }

}
