package org.petmarket.delivery.controller;

import static org.petmarket.utils.MessageUtils.BAD_REQUEST;
import static org.petmarket.utils.MessageUtils.DELIVERY_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.FORBIDDEN;
import static org.petmarket.utils.MessageUtils.LANGUAGE_OR_DELIVERY_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;
import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.petmarket.utils.MessageUtils.UNAUTHORIZED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.delivery.dto.DeliveryRequestDto;
import org.petmarket.delivery.dto.DeliveryResponseDto;
import org.petmarket.delivery.service.DeliveryService;
import org.petmarket.errorhandling.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "Delivery", description = "the delivery methods API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/admin/deliveries")
public class DeliveryAdminController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Create a new Delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = DeliveryResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = FORBIDDEN, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping
    @ResponseBody
    public DeliveryResponseDto addDelivery(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final DeliveryRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Delivery - {}.", request);
        return deliveryService.addDelivery(request, bindingResult);
    }

    @Operation(summary = "Update Delivery by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = DeliveryResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = FORBIDDEN, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = LANGUAGE_OR_DELIVERY_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PutMapping("/{id}/{langCode}")
    @ResponseBody
    public DeliveryResponseDto updateDelivery(
            @Parameter(description = "The ID of the Delivery to update", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the Delivery to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final DeliveryRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to update delivery - {} with id {}.", request, id);
        return deliveryService.updateDelivery(id, langCode, request, bindingResult);
    }

    @Operation(summary = "Delete Delivery by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = SUCCESSFULLY_OPERATION),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = FORBIDDEN, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = DELIVERY_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteDelivery(
            @Parameter(description = "The ID of the delivery to delete", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to delete the delivery with id - {}.", id);
        deliveryService.deleteDelivery(id);
        log.info("the delivery with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Get Delivery by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = DeliveryResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = LANGUAGE_OR_DELIVERY_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
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
        DeliveryResponseDto dto = deliveryService.findById(id, langCode);
        log.info("the delivery with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get all deliveries.", description = "Obtaining all deliveries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
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
        log.info("Received request to get all deliveries.");
        Collection<DeliveryResponseDto> dtos = deliveryService.getAll(langCode);
        log.info("All deliveries were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }
}
