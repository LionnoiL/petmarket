package org.petmarket.advertisements.attributes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.attributes.dto.AttributeResponseDto;
import org.petmarket.advertisements.attributes.service.AttributeService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
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
@RequestMapping(value = "/v1/attributes")
public class AttributeController {

    private final AttributeService attributeService;

    @Operation(summary = "Get Attribute by ID")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}/{langCode}")
    public AttributeResponseDto getAttributeById(
            @ParameterId @PathVariable @Positive Long id,
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get the attribute with id - {}.", id);
        AttributeResponseDto dto = attributeService.findById(id, langCode);
        log.info("the attribute with id - {} was retrieved - {}.", id, dto);
        return dto;
    }

    @Operation(summary = "Get Attributes by Attribute Group", description = "Obtaining attributes")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = AttributeResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/group/{id}/{langCode}")
    public ResponseEntity<Collection<AttributeResponseDto>> getByGroup(
            @ParameterId @PathVariable @Positive Long id,
            @ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get Attributes by group {}", id);
        Collection<AttributeResponseDto> dtos = attributeService.getByGroup(id, langCode);
        log.info("All Attributes were retrieved - {}.", dtos);
        return ResponseEntity.ok().body(dtos);
    }

}
