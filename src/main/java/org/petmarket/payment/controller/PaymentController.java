package org.petmarket.payment.controller;

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
import org.petmarket.payment.dto.PaymentResponseDto;
import org.petmarket.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.petmarket.utils.MessageUtils.LANGUAGE_OR_PAYMENT_NOT_FOUND;
import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Payment", description = "the payments methods API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Get Payment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = PaymentResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = LANGUAGE_OR_PAYMENT_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{id}/{langCode}")
    @ResponseBody
    public PaymentResponseDto getPaymentById(
            @Parameter(description = "The ID of the payments to retrieve", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the payments to retrieve", required = true,
                    schema = @Schema(type = "string"), example = "ua"
            )
            @PathVariable String langCode) {
        log.info("Received request to get the payment with id - {}.", id);
        PaymentResponseDto dto = paymentService.findEnabledById(id, langCode);
        log.info("the payment with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get all payments.", description = "Obtaining all payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(
                                    implementation = PaymentResponseDto.class))
                    )
            })
    })
    @GetMapping("/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<PaymentResponseDto>> getAll(
            @Parameter(description = "The Code Language of the payments to retrieve", required = true,
                    schema = @Schema(type = "string"), example = "ua"
            )
            @PathVariable String langCode) {
        log.info("Received request to get all enabled payments.");
        Collection<PaymentResponseDto> dtos = paymentService.getEnabled(langCode);
        log.info("All payments were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }
}
