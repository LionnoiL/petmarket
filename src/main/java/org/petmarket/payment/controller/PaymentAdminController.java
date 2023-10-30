package org.petmarket.payment.controller;

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
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.payment.dto.PaymentRequestDto;
import org.petmarket.payment.dto.PaymentResponseDto;
import org.petmarket.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "Payment", description = "the payments methods API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/admin/payments")
public class PaymentAdminController {

    private final PaymentService paymentService;

    @Operation(summary = "Create a new Payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = PaymentResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description =
                    "The Payment has already been added " +
                            "or some data is missing", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping
    @ResponseBody
    public PaymentResponseDto addPayment(
            @RequestBody @Valid @NotNull(message = "Request body is mandatory") final PaymentRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Payment - {}.", request);
        return paymentService.addPayment(request, bindingResult);
    }

    @Operation(summary = "Update Payment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = PaymentResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Some data is missing", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Language or Payment not found", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PutMapping("/{id}/{langCode}")
    @ResponseBody
    public PaymentResponseDto updatePayment(
            @Parameter(description = "The ID of the Payment to update", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the Payment to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode,
            @RequestBody @Valid @NotNull(message = "Request body is mandatory") final PaymentRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to update payment - {} with id {}.", request, id);
        return paymentService.updatePayment(id, langCode, request, bindingResult);
    }

    @Operation(summary = "Delete Payment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Payment not found", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletePayment(
            @Parameter(description = "The ID of the payment to delete", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to delete the payment with id - {}.", id);
        paymentService.deletePayment(id);
        log.info("the payment with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Get Payment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = PaymentResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Payment or Language not found", content = {
                    @Content(mediaType = "application/json", schema =
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
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode) {
        log.info("Received request to get the payment with id - {}.", id);
        PaymentResponseDto dto = paymentService.findById(id, langCode);
        log.info("the payment with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get all payments.", description = "Obtaining all payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = PaymentResponseDto.class))
                    )
            })
    })
    @GetMapping("/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<PaymentResponseDto>> getAll(
            @Parameter(description = "The Code Language of the payments to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode) {
        log.info("Received request to get all payments.");
        Collection<PaymentResponseDto> dtos = paymentService.getAll(langCode);
        log.info("All payments were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }
}
