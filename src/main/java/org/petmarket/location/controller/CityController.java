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
import org.petmarket.location.service.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.petmarket.utils.MessageUtils.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Location", description = "the location API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/cities")
public class CityController {

    private final CityService cityService;

    @Operation(summary = "Get City by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = CityResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = CITY_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{id}")
    @ResponseBody
    public CityResponseDto getCityById(
            @Parameter(description = "The ID of the city to retrieve", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to get the City with id - {}.", id);
        CityResponseDto dto = cityService.findById(id);
        log.info("the City with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get City by KOATUU code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = CityResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = CITY_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/byKoatuu/{koatuu}")
    @ResponseBody
    public CityResponseDto getCityByKoatuuCode(
            @Parameter(description = "The KOATUU code of the city to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String koatuu) {
        log.info("Received request to get the City with koatuu - {}.", koatuu);
        CityResponseDto dto = cityService.findByKoatuu(koatuu);
        log.info("the City with koatuu - {} was retrieved - {}.", koatuu, dto);
        return dto;
    }

    @Operation(summary = "Get City by Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(
                                    implementation = CityResponseDto.class))
                    )
            })
    })
    @GetMapping("/byName/{name}")
    @ResponseBody
    public List<CityResponseDto> getCityByName(
        @Parameter(description = "The Name of the city to retrieve", required = true,
            schema = @Schema(type = "string")
        )
        @PathVariable String name,
        @Parameter(description = "The size of the page to be returned", required = true,
            schema = @Schema(type = "integer", defaultValue = "12")
        ) @RequestParam(defaultValue = "12") int size
    ) {
        log.info("Received request to get the City with name - {}.", name);
        List<CityResponseDto> dto = cityService.findByName(name, size);
        log.info("the City with name - {} was retrieved - {}.", name, dto);
        return dto;
    }

    @Operation(summary = "Get City by Name and State ID")
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
    @GetMapping("/byName/{name}/byStateId/{id}")
    @ResponseBody
    public List<CityResponseDto> getCityByNameAndStateId(
        @Parameter(description = "The Name of the city to retrieve", required = true,
            schema = @Schema(type = "string")
        )
        @PathVariable String name,
        @Parameter(description = "The ID of the state to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64")
        )
        @PathVariable Long id) {
        log.info("Received request to get the City with name - {}.", name);
        List<CityResponseDto> dto = cityService.findByNameAndStateId(name, id);
        log.info("the City with name - {} was retrieved - {}.", name, dto);
        return dto;
    }
}
