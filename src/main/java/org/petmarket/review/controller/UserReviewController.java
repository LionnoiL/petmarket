package org.petmarket.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.review.dto.UserReviewListResponseDto;
import org.petmarket.users.entity.User;
import org.petmarket.users.service.UserService;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterPageSize;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.petmarket.utils.MessageUtils.SUCCESSFULLY_OPERATION;

@Tag(name = "Review", description = "the reviews API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/users")
public class UserReviewController {

    private final UserService userService;

    @Operation(summary = "Get review by user ID")
    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION)
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @GetMapping("/{id}/reviews")
    public UserReviewListResponseDto getReviewsByUser(
            @ParameterId @PathVariable Long id,
            @ParameterPageSize @RequestParam(defaultValue = "12") @Positive int size) {
        log.info("Received request to get reviews on the user with id - {}.", id);
        User user = userService.findById(id);
        UserReviewListResponseDto dtos = userService.getReviewsByUser(user, size);
        log.info("the reviews on the user with id - {} - {}.", id, dtos);
        return dtos;
    }
}
