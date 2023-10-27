package org.petmarket.breeds.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.service.BreedService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/breeds")
@RequiredArgsConstructor
public class BreedController {
    private final BreedService breedService;

    @GetMapping("/{breedId}/{langCode}")
    public BreedResponseDto get(@PathVariable Long breedId,
                                @PathVariable String langCode) {
        return breedService.get(breedId, langCode);
    }

    @GetMapping("/{langCode}")
    public List<BreedResponseDto> getAll(
            @PathVariable
            @Parameter(description = "Language code", example = "en") String langCode,
            @RequestParam(required = false)
            @Parameter(description = "ID of the category", example = "1") Long categoryId) {
        return breedService.getAllByCategory(langCode, categoryId);
    }
}
