package org.petmarket.advertisements.advertisement.controller;

import static org.petmarket.utils.MessageUtils.ADVERTISEMENT_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.BAD_REQUEST;
import static org.petmarket.utils.MessageUtils.FORBIDDEN;
import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;
import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.petmarket.utils.MessageUtils.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementRequestDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementResponseTranslateMapper;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.service.AdvertisementCategoryService;
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Advertisement")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final AdvertisementCategoryService categoryService;
    private final LanguageService languageService;
    private final AdvertisementResponseTranslateMapper advertisementMapper;

    @Operation(summary = "Get Advertisement by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AdvertisementResponseDto.class))
        }),
        @ApiResponse(responseCode = "404", description = ADVERTISEMENT_NOT_FOUND, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = ErrorResponse.class))
        })
    })
    @GetMapping("/{id}/{langCode}")
    @ResponseBody
    public AdvertisementResponseDto getAdvertisementById(
        @Parameter(description = "The ID of the Advertisement to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64")
        )
        @PathVariable Long id,
        @Parameter(description = "The Code Language of the Advertisement to retrieve", required = true,
            schema = @Schema(type = "string"), example = "ua"
        )
        @PathVariable String langCode) {
        log.info("Received request to get Advertisement Advertisement with id - {}.", id);
        AdvertisementResponseDto dto = advertisementService.findById(id, langCode);
        log.info("the Advertisement with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Create a new Advertisement")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AdvertisementResponseDto.class))
        }),
        @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "403", description = FORBIDDEN, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = ErrorResponse.class))
        })
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @ResponseBody
    public AdvertisementResponseDto addAdvertisement(
        @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AdvertisementRequestDto request,
        BindingResult bindingResult, Authentication authentication) {
        log.info("Received request to create Delivery - {}.", request);
        return advertisementService.addAdvertisement(request, bindingResult, authentication);
    }

    @Operation(summary = "Get Favorite Advertisements")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION)
    @GetMapping(path = "/{langCode}/favorite")
    public Page<AdvertisementResponseDto> getFavoriteAds(
        @Parameter(description = "The Code Language of the advertisements to retrieve", required = true,
            schema = @Schema(type = "string"), example = "ua"
        )
        @PathVariable String langCode,
        @Parameter(description = "Number of page (1..N)", required = true,
            schema = @Schema(type = "integer", defaultValue = "1")
        ) @RequestParam(defaultValue = "1") int page,
        @Parameter(description = "The size of the page to be returned", required = true,
            schema = @Schema(type = "integer", defaultValue = "30")
        ) @RequestParam(defaultValue = "30") int size,
        @Parameter(description = "List of categories identifiers",
            schema = @Schema(type = "array[integer]")
        ) @RequestParam(required = false) List<Long> categoriesIds
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Language language = languageService.getByLangCode(langCode);
        List<AdvertisementCategory> categories = categoryService.getByIds(categoriesIds);
        Page<Advertisement> advertisements = advertisementService.getFavoriteAds(categories,
            pageable);
        return advertisements.map(adv -> advertisementMapper.mapEntityToDto(adv, language));
    }
}
