package org.petmarket.location.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.location.dto.CountryResponseDto;
import org.petmarket.location.dto.StateResponseDto;
import org.petmarket.location.service.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Location", description = "the location API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/countries")
public class CountryController {

    private final CountryService countryService;

    @Operation(summary = "Get Country by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = CountryResponseDto.class))
        }),
        @ApiResponse(responseCode = "404", description = "Country not found", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        })
    })
    @GetMapping("/{id}")
    @ResponseBody
    public CountryResponseDto getStateById(
        @Parameter(description = "The ID of the Country to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64")
        )
        @PathVariable Long id) {
        log.info("Received request to get the Country with id - {}.", id);
        CountryResponseDto dto = countryService.findById(id);
        log.info("the Country with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get Country by Name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(
                    implementation = CountryResponseDto.class))
            )
        })
    })
    @GetMapping("/byName/{name}")
    @ResponseBody
    public List<CountryResponseDto> getCountryByName(
        @Parameter(description = "The Name of the Country to retrieve", required = true,
            schema = @Schema(type = "string")
        )
        @PathVariable String name) {
        log.info("Received request to get the Country with name - {}.", name);
        List<CountryResponseDto> dto = countryService.findByName(name);
        log.info("the Country with name - {} was retrieved - {}.", name, dto);
        return dto;
    }

    @Operation(summary = "Get the states of the Country")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(
                    implementation = StateResponseDto.class))
            )
        }),
        @ApiResponse(responseCode = "404", description = "Country not found", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        })
    })
    @GetMapping("/{id}/states")
    @ResponseBody
    public List<StateResponseDto> getStatesByState(
        @Parameter(description = "The ID of the state to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64")
        )
        @PathVariable Long id) {
        log.info("Received request to get the states of the country with id - {}.", id);
        List<StateResponseDto> dto = countryService.getStatesByCountryId(id);
        log.info("States of the country with id {} - {}", id, dto);
        return dto;
    }
}
