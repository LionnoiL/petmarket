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
import org.petmarket.advertisements.attributes.dto.AttributeResponseDto;
import org.petmarket.advertisements.attributes.service.AttributeService;
import org.petmarket.errorhandling.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "Attributes", description = "the advertisement attributes API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/attributes")
public class AttributeController {

    private final AttributeService attributeService;

    @Operation(summary = "Get Attribute by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = AttributeResponseDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Attribute not found", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{id}/{langCode}")
    @ResponseBody
    public AttributeResponseDto getAttributeById(
            @Parameter(description = "The ID of the attributes to retrieve", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the attributes to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode) {
        log.info("Received request to get the attribute with id - {}.", id);
        AttributeResponseDto dto = attributeService.findById(id, langCode);
        log.info("the attribute with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get Attributes by Attribute Group", description = "Obtaining attributes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = AttributeResponseDto.class))
                    )
            })
    })
    @GetMapping("/group/{id}/{langCode}")
    @ResponseBody
    public ResponseEntity<Collection<AttributeResponseDto>> getByGroup(
            @Parameter(description = "The Group ID", required = true,
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,
            @Parameter(description = "The Code Language of the attributes to retrieve", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String langCode) {
        log.info("Received request to get Attributes by group {}", id);
        Collection<AttributeResponseDto> dtos = attributeService.getByGroup(id, langCode);
        log.info("All Attributes were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

}
