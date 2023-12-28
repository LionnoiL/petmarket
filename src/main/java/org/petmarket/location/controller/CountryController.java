package org.petmarket.location.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.location.dto.CountryResponseDto;
import org.petmarket.location.dto.StateResponseDto;
import org.petmarket.location.service.CountryService;
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
@RequestMapping(value = "/v1/countries")
public class CountryController {

    private final CountryService countryService;

    @Operation(summary = "Get Country by ID")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}")
    public CountryResponseDto getStateById(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to get the Country with id - {}.", id);
        CountryResponseDto dto = countryService.findById(id);
        log.info("the Country with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get Country by Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(
                                    implementation = CountryResponseDto.class))
                    )
            })
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/byName/{name}")
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
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = StateResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}/states")
    public List<StateResponseDto> getStatesByState(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to get the states of the country with id - {}.", id);
        List<StateResponseDto> dto = countryService.getStatesByCountryId(id);
        log.info("States of the country with id {} - {}", id, dto);
        return dto;
    }
}
