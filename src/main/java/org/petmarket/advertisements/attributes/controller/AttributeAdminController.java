package org.petmarket.advertisements.attributes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeRequestDto;
import org.petmarket.advertisements.attributes.dto.AttributeResponseDto;
import org.petmarket.advertisements.attributes.service.AttributeService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseForbidden;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseUnauthorized;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;
import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Attributes", description = "the advertisement attributes API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin/attributes")
public class AttributeAdminController {

    private final AttributeService attributeService;

    @Operation(summary = "Create a new Attribute")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AttributeResponseDto.class))
    })
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping
    public AttributeResponseDto addAttribute(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AttributeRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Attribute - {}.", request);
        return attributeService.addAttribute(request, bindingResult);
    }

    @Operation(summary = "Update Attribute by ID")
    @ApiResponse(responseCode = "201", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AttributeResponseDto.class))
    })
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PutMapping("/{id}/{langCode}")
    public AttributeResponseDto updateAttribute(
            @ParameterId @PathVariable @Positive Long id,
            @ParameterLanguage @PathVariable String langCode,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AttributeRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to update attribute - {} with id {}.", request, id);
        return attributeService.updateAttribute(id, langCode, request, bindingResult);
    }

    @Operation(summary = "Delete Attribute by ID")
    @ApiResponse(responseCode = "204", description = SUCCESSFULLY_OPERATION)
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttribute(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to delete the attribute with id - {}.", id);
        attributeService.deleteAttribute(id);
        log.info("the attribute with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
