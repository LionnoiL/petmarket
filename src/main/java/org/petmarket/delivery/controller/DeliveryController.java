package org.petmarket.delivery.controller;

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
import org.petmarket.delivery.dto.DeliveryResponseDto;
import org.petmarket.delivery.service.DeliveryService;
import org.petmarket.errorhandling.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "Delivery", description = "the delivery methods API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Get Delivery by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = DeliveryResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Delivery or Language not found", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{id}/{langCode}")
    @ResponseBody
    public DeliveryResponseDto getDeliveryById(
            @Parameter(description = "The ID of the deliveries to retrieve", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the deliveries to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode) {
        log.info("Received request to get the delivery with id - {}.", id);
        DeliveryResponseDto dto = deliveryService.findEnabledById(id, langCode);
        log.info("the delivery with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get all deliveries.", description = "Obtaining all deliveries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = DeliveryResponseDto.class))
                    )
            })
    })
    @GetMapping("/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<DeliveryResponseDto>> getAll(
            @Parameter(description = "The Code Language of the deliveries to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode) {
        log.info("Received request to get all enabled deliveries.");
        Collection<DeliveryResponseDto> dtos = deliveryService.getEnabled(langCode);
        log.info("All deliveries were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }
}
