package org.petmarket.advertisements.attributes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeGroupResponseDto;
import org.petmarket.advertisements.attributes.service.AttributeGroupService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = "Attributes", description = "the advertisement attributes API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/attribute-groups")
public class AttributeGroupController {

    private final AttributeGroupService attributeGroupService;

    @Operation(summary = "Get all Attribute Groups.", description = "Obtaining all attribute groups")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = AttributeGroupResponseDto.class))
            )
    })
    @ApiResponseNotFound
    @GetMapping("/{langCode}")
    public ResponseEntity<Collection<AttributeGroupResponseDto>> getAll(
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get all Groups.");
        Collection<AttributeGroupResponseDto> dtos = attributeGroupService.getAll(langCode);
        log.info("All Groups were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get Attribute Group by ID")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
            @Schema(implementation = AttributeGroupResponseDto.class))
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}/{langCode}")
    public AttributeGroupResponseDto getGroupById(
            @ParameterId @PathVariable @Min(1L) Long id,
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get the group with id - {}.", id);
        AttributeGroupResponseDto dto = attributeGroupService.findById(id, langCode);
        log.info("the group with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get Attribute Groups by Category", description = "Obtaining attribute groups")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = AttributeGroupResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/category/{id}/{langCode}")
    public ResponseEntity<Collection<AttributeGroupResponseDto>> getByCategory(
            @ParameterId @PathVariable @Min(1L) Long id,
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get Groups by category {}", id);
        Collection<AttributeGroupResponseDto> dtos = attributeGroupService.getByCategory(id,
                langCode);
        log.info("All Groups by category were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get Attribute Groups for filter",
            description = "Obtaining a list of attribute groups used in filter construction")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = AttributeGroupResponseDto.class))
            )
    })
    @GetMapping("/uses-in-filter/{langCode}")
    public ResponseEntity<Collection<AttributeGroupResponseDto>> getForFilter(
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get Groups for filter");
        Collection<AttributeGroupResponseDto> dtos = attributeGroupService.getForFilter(langCode);
        log.info("All Groups were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }
}
