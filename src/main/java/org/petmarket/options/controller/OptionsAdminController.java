package org.petmarket.options.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.options.dto.OptionsRequestDto;
import org.petmarket.options.dto.OptionsResponseDto;
import org.petmarket.options.entity.Options;
import org.petmarket.options.entity.OptionsKey;
import org.petmarket.options.mapper.OptionsMapper;
import org.petmarket.options.service.OptionsService;
import org.petmarket.utils.annotations.responses.ApiResponseForbidden;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.petmarket.utils.annotations.responses.ApiResponseUnauthorized;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.REQUEST_BODY_IS_MANDATORY;

@Tag(name = "Options", description = "the site options API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/admin/options")
public class OptionsAdminController {

    private final OptionsService optionsService;
    private final OptionsMapper optionsMapper;

    @Operation(summary = "Update Options value by key")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PutMapping("/{key}")
    public OptionsResponseDto updateOptions(
            @PathVariable OptionsKey key,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final OptionsRequestDto value) {
        log.info("Received request to update Options - {} with key {}.", value, key);
        Options options = optionsService.setOptionsValueByKey(key, value);
        return optionsMapper.mapEntityToDto(options);
    }
}
