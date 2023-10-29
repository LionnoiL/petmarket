package org.petmarket.advertisements.attributes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeGroupResponseDto;
import org.petmarket.advertisements.attributes.service.AttributeGroupService;
import org.petmarket.errorhandling.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Attributes", description = "the advertisement attributes API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/attribute-groups")
public class AttributeGroupController {

    private final AttributeGroupService attributeGroupService;

    @Operation(summary = "Get all Attribute Groups.", description = "Obtaining all attribute groups")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(
                    implementation = AttributeGroupResponseDto.class))
            )
        })
    })
    @GetMapping("/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<AttributeGroupResponseDto>> getAll(
        @Parameter(description = "The Code Language of the groups to retrieve", required = true,
            schema = @Schema(type = "string")
        )
        @PathVariable String langCode) {
        log.info("Received request to get all Groups.");
        Collection<AttributeGroupResponseDto> dtos = attributeGroupService.getAll(langCode);
        log.info("All Groups were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get Attribute Group by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = AttributeGroupResponseDto.class))
        }),
        @ApiResponse(responseCode = "404", description = "Group not found", content = {
            @Content(mediaType = "application/json", schema =
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
            schema = @Schema(type = "string")
        )
        @PathVariable String langCode) {
        log.info("Received request to get the group with id - {}.", id);
        AttributeGroupResponseDto dto = attributeGroupService.findById(id, langCode);
        log.info("the group with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get Attribute Groups by Category", description = "Obtaining attribute groups")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(
                mediaType = "application/json",
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
            schema = @Schema(type = "string")
        )
        @PathVariable String langCode) {
        log.info("Received request to get Groups by category {}", id);
        Collection<AttributeGroupResponseDto> dtos = attributeGroupService.getByCategory(id,
            langCode);
        log.info("All Groups were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "Get Attribute Groups for filter", description = "Obtaining a list of attribute groups used in filter construction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(
                    implementation = AttributeGroupResponseDto.class))
            )
        })
    })
    @GetMapping("/uses-in-filter/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<AttributeGroupResponseDto>> getForFilter(
        @Parameter(description = "The Code Language of the groups to retrieve", required = true,
            schema = @Schema(type = "string")
        )
        @PathVariable String langCode) {
        log.info("Received request to get Groups for filter");
        Collection<AttributeGroupResponseDto> dtos = attributeGroupService.getForFilter(langCode);
        log.info("All Groups were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }
}
