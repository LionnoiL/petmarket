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
import org.petmarket.location.dto.CityResponseDto;
import org.petmarket.location.service.CityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "City", description = "the city API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/cities")
public class CityController {

    private final CityService cityService;

    @Operation(summary = "Get City by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = CityResponseDto.class))
        }),
        @ApiResponse(responseCode = "404", description = "City not found", content = {
            @Content(mediaType = "application/json", schema =
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

    @Operation(summary = "Get City by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(
                    implementation = CityResponseDto.class))
            )
        })
    })
    @GetMapping("/{name}")
    @ResponseBody
    public List<CityResponseDto> getCityByName(
        @Parameter(description = "The Name of the city to retrieve", required = true,
            schema = @Schema(type = "string")
        )
        @PathVariable String name) {
        log.info("Received request to get the City with name - {}.", name);
        List<CityResponseDto> dto = cityService.findByName(name);
        log.info("the City with name - {} was retrieved - {}.", name, dto);
        return dto;
    }
}
