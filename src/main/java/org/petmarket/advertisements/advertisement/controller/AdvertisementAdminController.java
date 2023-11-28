package org.petmarket.advertisements.advertisement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementResponseTranslateMapper;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Advertisement")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/admin/advertisements")
public class AdvertisementAdminController {

    private final AdvertisementService advertisementService;
    private final OptionsService optionsService;
    private final AdvertisementResponseTranslateMapper advertisementMapper;

    @Operation(summary = "Set Advertisement status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = AdvertisementResponseDto.class))
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
    @PutMapping("/status")
    @ResponseBody
    public List<AdvertisementResponseDto> setStatusDraft(
            @Parameter(description = "List of advertisements identifiers",
                    schema = @Schema(type = "array[integer]")
            ) @RequestParam(required = false) List<Long> ids,
            @Parameter(description = "New status") @RequestParam(required = true) AdvertisementStatus status
    ) {
        List<Advertisement> advertisements = advertisementService.getAdvertisements(ids);
        advertisementService.setStatus(advertisements, status);
        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();
        return advertisements.stream()
                .map(adv -> advertisementMapper.mapEntityToDto(adv, defaultSiteLanguage))
                .toList();
    }
}
