package org.petmarket.advertisements.attributes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeRequestDto;
import org.petmarket.advertisements.attributes.dto.AttributeResponseDto;
import org.petmarket.advertisements.attributes.service.AttributeService;
import org.petmarket.errorhandling.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.*;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = AttributeResponseDto.class))
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
    public AttributeResponseDto addAttribute(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AttributeRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create Attribute - {}.", request);
        return attributeService.addAttribute(request, bindingResult);
    }

    @Operation(summary = "Update Attribute by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = AttributeResponseDto.class))
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
            @ApiResponse(responseCode = "404", description = LANGUAGE_OR_ATTRIBUTE_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PutMapping("/{id}/{langCode}")
    @ResponseBody
    public AttributeResponseDto updateAttribute(
            @Parameter(description = "The ID of the Attribute to update", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the Attribute to retrieve", required = true,
                    schema = @Schema(type = "string"), example = "ua"
            )
            @PathVariable String langCode,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final AttributeRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to update attribute - {} with id {}.", request, id);
        return attributeService.updateAttribute(id, langCode, request, bindingResult);
    }

    @Operation(summary = "Delete Attribute by ID")
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
            @ApiResponse(responseCode = "404", description = ATTRIBUTE_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteAttribute(
            @Parameter(description = "The ID of the attribute to delete", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        log.info("Received request to delete the attribute with id - {}.", id);
        attributeService.deleteAttribute(id);
        log.info("the attribute with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
