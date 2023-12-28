package org.petmarket.location.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.location.dto.CityResponseDto;
import org.petmarket.location.service.CityService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterPageSize;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Location", description = "the location API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/cities")
public class CityController {

    private final CityService cityService;

    @Operation(summary = "Get City by ID")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}")
    public CityResponseDto getCityById(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to get the City with id - {}.", id);
        CityResponseDto dto = cityService.findById(id);
        log.info("the City with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get City by KOATUU code")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/byKoatuu/{koatuu}")
    public CityResponseDto getCityByKoatuuCode(
            @Parameter(description = "The KOATUU code of the city to retrieve", required = true,
                    schema = @Schema(type = "string")
            ) @PathVariable String koatuu) {
        log.info("Received request to get the City with koatuu - {}.", koatuu);
        CityResponseDto dto = cityService.findByKoatuu(koatuu);
        log.info("the City with koatuu - {} was retrieved - {}.", koatuu, dto);
        return dto;
    }

    @Operation(summary = "Get City by Name")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = CityResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/byName/{name}")
    public List<CityResponseDto> getCityByName(
            @Parameter(description = "The Name of the city to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String name,
            @ParameterPageSize @RequestParam(defaultValue = "12") @Positive int size
    ) {
        log.info("Received request to get the City with name - {}.", name);
        List<CityResponseDto> dto = cityService.findByName(name, size);
        log.info("the City with name - {} was retrieved - {}.", name, dto);
        return dto;
    }

    @Operation(summary = "Get City by Name and State ID")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = CityResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/byName/{name}/byStateId/{id}")
    public List<CityResponseDto> getCityByNameAndStateId(
            @Parameter(description = "The Name of the city to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String name,
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to get the City with name - {}.", name);
        List<CityResponseDto> dto = cityService.findByNameAndStateId(name, id);
        log.info("the City with name - {} was retrieved - {}.", name, dto);
        return dto;
    }
}
