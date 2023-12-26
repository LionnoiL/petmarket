package org.petmarket.advertisements.attributes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeGroupRequestDto;
import org.petmarket.advertisements.attributes.dto.AttributeGroupResponseDto;
import org.petmarket.advertisements.attributes.service.AttributeGroupService;
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
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = "Attributes", description = "the advertisement attributes API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin/attribute-groups")
public class AttributeGroupAdminController {

    private final AttributeGroupService attributeGroupService;

    @Operation(summary = "Create a new Attribute group")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AttributeGroupResponseDto.class))
    })
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PostMapping
    public AttributeGroupResponseDto addGroup(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AttributeGroupRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Attribute group - {}.", request);
        return attributeGroupService.addGroup(request, bindingResult);
    }

    @Operation(summary = "Update Attribute group by ID")
    @ApiResponse(responseCode = "201", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AttributeGroupResponseDto.class))
    })
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PutMapping("/{id}/{langCode}")
    public AttributeGroupResponseDto updateGroup(
            @ParameterId @PathVariable @Min(1L) Long id,
            @ParameterLanguage @PathVariable String langCode,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AttributeGroupRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to update attribute group - {} with id {}.", request, id);
        return attributeGroupService.updateGroup(id, langCode, request, bindingResult);
    }

    @Operation(summary = "Delete Attribute group by ID")
    @ApiResponse(responseCode = "204", description = SUCCESSFULLY_OPERATION)
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(
            @ParameterId @PathVariable @Min(1L) Long id) {
        log.info("Received request to delete the attribute group with id - {}.", id);
        attributeGroupService.deleteGroup(id);
        log.info("the attribute group with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
