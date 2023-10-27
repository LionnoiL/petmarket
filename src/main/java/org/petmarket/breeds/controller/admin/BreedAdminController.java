package org.petmarket.breeds.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedRequestDto;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.service.BreedService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/breeds")
@RequiredArgsConstructor
@Tag(name = "Breeds", description = "API endpoints for breed administration")
public class BreedAdminController {
    private final BreedService breedService;

    @Operation(summary = "Save Breed", description = "Save a new breed.")
    @ApiResponse(responseCode = "200", description = "Successfully saved")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public BreedResponseDto save(@RequestBody
                                 @Schema(description = "Breed data to be saved")
                                 @Valid
                                 BreedRequestDto requestDto) {
        return breedService.save(requestDto);
    }

    @Operation(summary = "Add Translation", description = "Add a translation for a breed.")
    @ApiResponse(responseCode = "200", description = "Translation added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "Breed not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/{breedId}/{langCode}")
    public BreedResponseDto addTranslation(@PathVariable
                                           @Schema(description = "ID of the breed")
                                           Long breedId,
                                           @PathVariable
                                           @Schema(description = "Language code for translation")
                                           String langCode,
                                           @RequestBody
                                           @Schema(description = "Translation data to be added")
                                           BreedRequestDto requestDto) {
        return breedService.addTranslation(breedId, langCode, requestDto);
    }

    @Operation(summary = "Delete a breed", description = "Delete a breed by ID")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Breed deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Breed not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @DeleteMapping("/{breedId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "breedId")
                       @Parameter(description = "Breed ID", required = true) Long breedId) {
        breedService.delete(breedId);
    }
}
