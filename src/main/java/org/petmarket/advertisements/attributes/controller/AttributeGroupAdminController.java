package org.petmarket.advertisements.attributes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeGroupRequestDto;
import org.petmarket.advertisements.attributes.dto.AttributeGroupResponseDto;
import org.petmarket.advertisements.attributes.service.AttributeGroupService;
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
@RequestMapping(value = "/v1/admin/attribute-groups")
public class AttributeGroupAdminController {

    private final AttributeGroupService attributeGroupService;

    @Operation(summary = "Create a new Attribute group")
    @ApiResponseCreated
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PostMapping
    public ResponseEntity<AttributeGroupResponseDto> addGroup(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AttributeGroupRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Attribute group - {}.", request);
        AttributeGroupResponseDto responseDto = attributeGroupService.addGroup(request, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Update Attribute group by ID")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PutMapping("/{id}/{langCode}")
    public AttributeGroupResponseDto updateGroup(
            @ParameterId @PathVariable @Positive Long id,
            @ParameterLanguage @PathVariable String langCode,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AttributeGroupRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to update attribute group - {} with id {}.", request, id);
        return attributeGroupService.updateGroup(id, langCode, request, bindingResult);
    }

    @Operation(summary = "Delete Attribute group by ID")
    @ApiResponseDeleted
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to delete the attribute group with id - {}.", id);
        attributeGroupService.deleteGroup(id);
        log.info("the attribute group with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
