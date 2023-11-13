package org.petmarket.advertisements.advertisement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementRequestDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.errorhandling.ErrorResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Advertisement")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

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
            schema = @Schema(type = "string")
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
}
