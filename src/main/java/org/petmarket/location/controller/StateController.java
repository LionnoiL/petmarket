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
import org.petmarket.location.dto.StateResponseDto;
import org.petmarket.location.service.StateService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Location", description = "the location API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/states")
public class StateController {

    private final StateService stateService;

    @Operation(summary = "Get State by ID")
    @ApiResponseSuccessful
    @ApiResponseNotFound
    @GetMapping("/{id}")
    public StateResponseDto getStateById(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to get the State with id - {}.", id);
        StateResponseDto dto = stateService.findById(id);
        log.info("the State with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get State by KOATUU code")
    @ApiResponseSuccessful
    @ApiResponseNotFound
    @GetMapping("/byKoatuu/{koatuu}")
    public StateResponseDto getStateByKoatuuCode(
            @Parameter(description = "The KOATUU code of the state to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String koatuu) {
        log.info("Received request to get the State with koatuu - {}.", koatuu);
        StateResponseDto dto = stateService.findByKoatuu(koatuu);
        log.info("the State with koatuu - {} was retrieved - {}.", koatuu, dto);
        return dto;
    }

    @Operation(summary = "Get State by Name")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = StateResponseDto.class))
            )
    })
    @GetMapping("/byName/{name}")
    public List<StateResponseDto> getStateByName(
            @Parameter(description = "The Name of the state to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String name) {
        log.info("Received request to get the State with name - {}.", name);
        List<StateResponseDto> dto = stateService.findByName(name);
        log.info("the State with name - {} was retrieved - {}.", name, dto);
        return dto;
    }

    @Operation(summary = "Get the cities of the state")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = CityResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}/cities")
    public List<CityResponseDto> getCitiesByState(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to get the cities of the state with id - {}.", id);
        List<CityResponseDto> dto = stateService.getCitiesByStateId(id);
        log.info("Cities of the state with id {} - {}", id, dto);
        return dto;
    }

    @Operation(summary = "Get State by Name and Country ID")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = CityResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/byName/{name}/byCountryId/{id}")
    public List<StateResponseDto> getStateByNameAndCountryId(
            @Parameter(description = "The Name of the states to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String name,
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to get the State with name - {}.", name);
        List<StateResponseDto> dto = stateService.findByNameAndCountryId(name, id);
        log.info("the State with name - {} was retrieved - {}.", name, dto);
        return dto;
    }
}
