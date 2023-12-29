package org.petmarket.options.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.options.dto.OptionsResponseDto;
import org.petmarket.options.entity.Options;
import org.petmarket.options.entity.OptionsKey;
import org.petmarket.options.mapper.OptionsMapper;
import org.petmarket.options.service.OptionsService;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Options", description = "the site options API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/options")
public class OptionsController {

    private final OptionsService optionsService;
    private final OptionsMapper optionsMapper;

    @Operation(summary = "Get Options value by key")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION, content = {
            @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(
                            implementation = OptionsResponseDto.class))
            )
    })
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{key}")
    public OptionsResponseDto getOptionsValueByKey(
            @PathVariable OptionsKey key) {
        log.debug("Received request to get the Options with key - {}.", key);
        Options options = optionsService.getOptionsByKey(key);
        log.debug("the Options with key - {} was retrieved - {}.", key, options);
        return optionsMapper.mapEntityToDto(options);
    }
}
