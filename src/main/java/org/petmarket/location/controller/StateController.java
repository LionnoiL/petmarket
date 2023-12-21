package org.petmarket.location.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.location.dto.CityResponseDto;
import org.petmarket.location.dto.StateResponseDto;
import org.petmarket.location.service.StateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.*;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = StateResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = STATE_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{id}")
    @ResponseBody
    public StateResponseDto getStateById(
            @Parameter(description = "The ID of the state to retrieve", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to get the State with id - {}.", id);
        StateResponseDto dto = stateService.findById(id);
        log.info("the State with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get State by KOATUU code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = StateResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = STATE_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/byKoatuu/{koatuu}")
    @ResponseBody
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(
                                    implementation = StateResponseDto.class))
                    )
            })
    })
    @GetMapping("/byName/{name}")
    @ResponseBody
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(
                                    implementation = CityResponseDto.class))
                    )
            }),
            @ApiResponse(responseCode = "404", description = STATE_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{id}/cities")
    @ResponseBody
    public List<CityResponseDto> getCitiesByState(
            @Parameter(description = "The ID of the state to retrieve", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to get the cities of the state with id - {}.", id);
        List<CityResponseDto> dto = stateService.getCitiesByStateId(id);
        log.info("Cities of the state with id {} - {}", id, dto);
        return dto;
    }

    @Operation(summary = "Get State by Name and Country ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(
                                    implementation = CityResponseDto.class))
                    )
            }),
            @ApiResponse(responseCode = "404", description = COUNTRY_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/byName/{name}/byCountryId/{id}")
    @ResponseBody
    public List<StateResponseDto> getStateByNameAndCountryId(
            @Parameter(description = "The Name of the states to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String name,
            @Parameter(description = "The ID of the country to retrieve", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to get the State with name - {}.", name);
        List<StateResponseDto> dto = stateService.findByNameAndCountryId(name, id);
        log.info("the State with name - {} was retrieved - {}.", name, dto);
        return dto;
    }
}
