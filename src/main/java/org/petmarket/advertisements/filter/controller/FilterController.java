package org.petmarket.advertisements.filter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.service.AdvertisementCategoryService;
import org.petmarket.advertisements.filter.dto.FilterDto;
import org.petmarket.advertisements.filter.service.FilterService;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Filter")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/filter")
public class FilterController {

    private final FilterService filterService;
    private final LanguageService languageService;
    private final AdvertisementCategoryService categoryService;

    @Operation(summary = "Left sidebar filter",
            description = "Get a list of filter elements by active advertisements in a category"
    )
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{langCode}")
    public FilterDto getLeftSideBarFilter(@ParameterLanguage @PathVariable String langCode,
                                          @RequestParam @Positive Long categoryId) {
        Language language = languageService.getByLangCode(langCode);
        AdvertisementCategory category = categoryService.findCategory(categoryId);
        return filterService.getLeftSideBarFilter(language, category);
    }
}
