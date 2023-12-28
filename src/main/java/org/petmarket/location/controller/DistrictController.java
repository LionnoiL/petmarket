package org.petmarket.location.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.location.dto.DistrictResponseDto;
import org.petmarket.location.service.DistrictService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Location", description = "the location API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/districts")
public class DistrictController {

    private final DistrictService districtService;

    @Operation(summary = "Get District by ID")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}")
    public DistrictResponseDto getDistrictById(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to get the District with id - {}.", id);
        DistrictResponseDto dto = districtService.findById(id);
        log.info("the District with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get District by KOATUU code")
    @ApiResponseSuccessful
    @ApiResponseNotFound
    @GetMapping("/byKoatuu/{koatuu}")
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
