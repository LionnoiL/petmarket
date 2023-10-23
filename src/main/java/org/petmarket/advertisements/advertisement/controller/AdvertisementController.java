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
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.errorhandling.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Advertisement", description = "the advertisement API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @Operation(summary = "Get Advertisement by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = AdvertisementResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Advertisement not found", content = {
                    @Content(mediaType = "application/json", schema =
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
}
