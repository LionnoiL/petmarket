package org.petmarket.location.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.location.dto.DistrictRequestDto;
import org.petmarket.location.dto.DistrictResponseDto;
import org.petmarket.location.service.DistrictService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;

@Tag(name = "Location", description = "the location API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin/districts")
public class DistrictAdminController {

    private final DistrictService districtService;

    @Operation(summary = "Create a new District")
    @ApiResponseCreated
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PostMapping
    public ResponseEntity<DistrictResponseDto> addDistrict(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final DistrictRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create District - {}.", request);
        DistrictResponseDto responseDto = districtService.addDistrict(request, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Update District by ID")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PutMapping("/{id}")
    public DistrictResponseDto updateDistrict(
            @ParameterId @PathVariable @Positive Long id,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final DistrictRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to update District - {} with id {}.", request, id);
        return districtService.updateDistrict(id, request, bindingResult);
    }

    @Operation(summary = "Delete District by ID")
    @ApiResponseDeleted
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistrict(
            @ParameterId @PathVariable @Positive Long id) {
        log.info("Received request to delete the District with id - {}.", id);
        districtService.deleteDistrict(id);
        log.info("the District with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
