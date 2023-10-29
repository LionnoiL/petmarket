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
import org.petmarket.advertisements.attributes.dto.AttributeGroupRequestDto;
import org.petmarket.advertisements.attributes.dto.AttributeGroupResponseDto;
import org.petmarket.advertisements.attributes.service.AttributeGroupService;
import org.petmarket.errorhandling.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Attributes", description = "the advertisement attributes API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/admin/attribute-groups")
public class AttributeGroupAdminController {

    private final AttributeGroupService attributeGroupService;

    @Operation(summary = "Create a new Attribute group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = AttributeGroupResponseDto.class))
        }),
        @ApiResponse(responseCode = "400", description =
            "The Attribute group has already been added " +
                "or some data is missing", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        })
    })
    @PostMapping
    @ResponseBody
    public AttributeGroupResponseDto addGroup(
        @RequestBody @Valid @NotNull(message = "Request body is mandatory") final AttributeGroupRequestDto request,
        BindingResult bindingResult) {
        log.info("Received request to create Attribute group - {}.", request);
        return attributeGroupService.addGroup(request, bindingResult);
    }

    @Operation(summary = "Update Attribute group by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successful operation", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = AttributeGroupResponseDto.class))
        }),
        @ApiResponse(responseCode = "400", description = "Some data is missing", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Language or Attribute group not found", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        })
    })
    @PutMapping("/{id}/{langCode}")
    @ResponseBody
    public AttributeGroupResponseDto updateGroup(
        @Parameter(description = "The ID of the Attribute group to update", required = true,
            schema = @Schema(type = "integer", format = "int64")
        )
        @PathVariable Long id,
        @Parameter(description = "The Code Language of the Attribute group to retrieve", required = true,
            schema = @Schema(type = "string")
        )
        @PathVariable String langCode,
        @RequestBody @Valid @NotNull(message = "Request body is mandatory") final AttributeGroupRequestDto request,
        BindingResult bindingResult) {
        log.info("Received request to update attribute group - {} with id {}.", request, id);
        return attributeGroupService.updateGroup(id, langCode, request, bindingResult);
    }

    @Operation(summary = "Delete Attribute group by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successful operation"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Attribute group not found", content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorResponse.class))
        })
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteGroup(
        @Parameter(description = "The ID of the attribute group to delete", required = true,
            schema = @Schema(type = "integer", format = "int64")
        )
        @PathVariable Long id) {
        log.info("Received request to delete the attribute group with id - {}.", id);
        attributeGroupService.deleteGroup(id);
        log.info("the attribute group with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
