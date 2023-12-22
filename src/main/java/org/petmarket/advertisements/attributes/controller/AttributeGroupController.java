package org.petmarket.advertisements.attributes.controller;

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
import org.petmarket.advertisements.attributes.dto.AttributeGroupResponseDto;
import org.petmarket.advertisements.attributes.service.AttributeGroupService;
import org.petmarket.errorhandling.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.petmarket.utils.MessageUtils.ATTRIBUTE_GROUP_NOT_FOUND;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(
                                    implementation = AttributeGroupResponseDto.class))
                    )
            })
    })
    @GetMapping("/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<AttributeGroupResponseDto>> getAll(
            @Parameter(description = "The Code Language of the groups to retrieve", required = true,
                    schema = @Schema(type = "string"), example = "ua"
            )
            @PathVariable String langCode) {
        log.info("Received request to get all Groups.");
        Collection<AttributeGroupResponseDto> dtos = attributeGroupService.getAll(langCode);
        log.info("All Groups were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get Attribute Group by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = AttributeGroupResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = ATTRIBUTE_GROUP_NOT_FOUND, content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{id}/{langCode}")
    @ResponseBody
    public AttributeGroupResponseDto getGroupById(
            @Parameter(description = "The ID of the groups to retrieve", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the groups to retrieve", required = true,
                    schema = @Schema(type = "string"), example = "ua"
            )
            @PathVariable String langCode) {
        log.info("Received request to get the group with id - {}.", id);
        AttributeGroupResponseDto dto = attributeGroupService.findById(id, langCode);
        log.info("the group with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get Attribute Groups by Category", description = "Obtaining attribute groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(
                                    implementation = AttributeGroupResponseDto.class))
                    )
            })
    })
    @GetMapping("/category/{id}/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<AttributeGroupResponseDto>> getByCategory(
            @Parameter(description = "The Category ID", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the groups to retrieve", required = true,
                    schema = @Schema(type = "string"), example = "ua"
            )
            @PathVariable String langCode) {
        log.info("Received request to get Groups by category {}", id);
        Collection<AttributeGroupResponseDto> dtos = attributeGroupService.getByCategory(id,
                langCode);
        log.info("All Groups were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get Attribute Groups for filter",
            description = "Obtaining a list of attribute groups used in filter construction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(
                                    implementation = AttributeGroupResponseDto.class))
                    )
            })
    })
    @GetMapping("/uses-in-filter/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<AttributeGroupResponseDto>> getForFilter(
            @Parameter(description = "The Code Language of the groups to retrieve", required = true,
                    schema = @Schema(type = "string"), example = "ua"
            )
            @PathVariable String langCode) {
        log.info("Received request to get Groups for filter");
        Collection<AttributeGroupResponseDto> dtos = attributeGroupService.getForFilter(langCode);
        log.info("All Groups were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }
}
