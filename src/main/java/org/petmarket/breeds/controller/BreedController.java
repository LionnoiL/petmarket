package org.petmarket.breeds.controller;

import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.service.BreedService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<BreedResponseDto> getAll(@PathVariable String langCode,
                                         Pageable pageable) {
        return breedService.getAll(pageable, langCode);
    }
}
