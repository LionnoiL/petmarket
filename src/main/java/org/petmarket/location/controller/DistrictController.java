package org.petmarket.location.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.location.dto.DistrictResponseDto;
import org.petmarket.location.service.DistrictService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.DISTRICT_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Location", description = "the location API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/districts")
public class DistrictController {

    private final DistrictService districtService;

    @Operation(summary = "Get District by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = DistrictResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = DISTRICT_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{id}")
    @ResponseBody
    public DistrictResponseDto getDistrictById(
            @Parameter(description = "The ID of the District to retrieve", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to get the District with id - {}.", id);
        DistrictResponseDto dto = districtService.findById(id);
        log.info("the District with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get District by KOATUU code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = DistrictResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = DISTRICT_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/byKoatuu/{koatuu}")
    @ResponseBody
    public DistrictResponseDto getDistrictByKoatuuCode(
            @Parameter(description = "The KOATUU code of the District to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String koatuu) {
        log.info("Received request to get the District with koatuu - {}.", koatuu);
        DistrictResponseDto dto = districtService.findByKoatuu(koatuu);
        log.info("the District with koatuu - {} was retrieved - {}.", koatuu, dto);
        return dto;
    }
}
