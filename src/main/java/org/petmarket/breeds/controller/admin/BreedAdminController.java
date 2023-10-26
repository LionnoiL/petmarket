package org.petmarket.breeds.controller.admin;

import lombok.RequiredArgsConstructor;
import org.petmarket.breeds.dto.BreedRequestDto;
import org.petmarket.breeds.dto.BreedResponseDto;
import org.petmarket.breeds.service.BreedService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/breeds")
@RequiredArgsConstructor
public class BreedAdminController {
    private final BreedService breedService;

    @PostMapping
    public BreedResponseDto save(@RequestBody BreedRequestDto requestDto) {
        return breedService.save(requestDto);
    }

    @PostMapping("/{breedId}/{langCode}")
    public BreedResponseDto addTranslation(@PathVariable Long breedId,
                                           @PathVariable String langCode,
                                           @RequestBody BreedRequestDto requestDto) {
        return breedService.addTranslation(breedId, langCode, requestDto);
    }
}
