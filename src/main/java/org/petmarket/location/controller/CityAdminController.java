package org.petmarket.location.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.location.dto.CityRequestDto;
import org.petmarket.location.dto.CityResponseDto;
import org.petmarket.location.service.CityService;
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
@RequestMapping(value = "/v1/admin/cities")
public class CityAdminController {

    private final CityService cityService;

    @Operation(summary = "Create a new City")
    @ApiResponseCreated
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PostMapping
    public ResponseEntity<CityResponseDto> addCity(
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final CityRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to create City - {}.", request);
        CityResponseDto responseDto = cityService.addCity(request, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Update City by ID")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PutMapping("/{id}")
    public CityResponseDto updateCite(
            @ParameterId @PathVariable @Positive Long id,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final CityRequestDto request,
            BindingResult bindingResult) {
        log.info("Received request to update City - {} with id {}.", request, id);
        return cityService.updateCity(id, request, bindingResult);
    }

    @Operation(summary = "Delete City by ID")
    @ApiResponseDeleted
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(
            @ParameterId @PathVariable Long id) {
        log.info("Received request to delete the City with id - {}.", id);
        cityService.deleteCity(id);
        log.info("the City with id - {} was deleted.", id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
