package org.petmarket.advertisements.attributes.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import org.petmarket.utils.annotations.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;

@Tag(name = "Attributes", description = "the advertisement attributes API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin/attributes")
public class AttributeAdminController {

    private final AttributeService attributeService;

    @Operation(summary = "Create a new Attribute")
    @ApiResponseCreated
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @PostMapping
    public ResponseEntity<AttributeResponseDto> addAttribute(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AttributeRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Attribute - {}.", request);
        AttributeResponseDto responseDto = attributeService.addAttribute(request, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Update Attribute by ID")
    @ApiResponseSuccessful
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
    @ApiResponseDeleted
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttribute(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to delete the attribute with id - {}.", id);
        attributeService.deleteAttribute(id);
        log.info("the attribute with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
