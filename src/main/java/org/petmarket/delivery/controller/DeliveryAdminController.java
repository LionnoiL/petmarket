package org.petmarket.delivery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.delivery.dto.DeliveryRequestDto;
import org.petmarket.delivery.dto.DeliveryResponseDto;
import org.petmarket.delivery.service.DeliveryService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;
import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = "Delivery", description = "the delivery methods API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin/deliveries")
public class DeliveryAdminController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Create a new Delivery")
    @ApiResponseCreated
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @PostMapping
    public ResponseEntity<DeliveryResponseDto> addDelivery(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final DeliveryRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Delivery - {}.", request);
        DeliveryResponseDto responseDto = deliveryService.addDelivery(request, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Update Delivery by ID")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @PutMapping("/{id}/{langCode}")
    public DeliveryResponseDto updateDelivery(
            @ParameterId @PathVariable @Positive Long id,
            @ParameterLanguage @PathVariable String langCode,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final DeliveryRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to update delivery - {} with id {}.", request, id);
        return deliveryService.updateDelivery(id, langCode, request, bindingResult);
    }

    @Operation(summary = "Delete Delivery by ID")
    @ApiResponseDeleted
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(
            @Parameter(description = "The ID of the delivery to delete", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to delete the delivery with id - {}.", id);
        deliveryService.deleteDelivery(id);
        log.info("the delivery with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get Delivery by ID")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}/{langCode}")
    public DeliveryResponseDto getDeliveryById(
            @ParameterId @PathVariable @Positive Long id,
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get the delivery with id - {}.", id);
        DeliveryResponseDto dto = deliveryService.findById(id, langCode);
        log.info("the delivery with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get all deliveries.", description = "Obtaining all deliveries")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = DeliveryResponseDto.class))
            )
    })
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{langCode}")
    public ResponseEntity<Collection<DeliveryResponseDto>> getAll(
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get all deliveries.");
        Collection<DeliveryResponseDto> dtos = deliveryService.getAll(langCode);
        log.info("All deliveries were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }
}
