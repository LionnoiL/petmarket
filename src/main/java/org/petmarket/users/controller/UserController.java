package org.petmarket.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.users.dto.ResetPasswordRequestDto;
import org.petmarket.users.dto.UserResponseDto;
import org.petmarket.users.entity.User;
import org.petmarket.users.mapper.UserMapper;
import org.petmarket.users.service.UserAuthService;
import org.petmarket.users.service.UserService;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.petmarket.utils.annotations.responses.ApiResponseUnauthorized;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.petmarket.utils.MessageUtils.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Users", description = "the users API")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(value = "/v1/users")
public class UserController {

    private final UserService userService;
    private final UserAuthService userAuthService;
    private final UserMapper userMapper;

    @Operation(summary = "Send request to reset user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "User email not found", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping("/{email}/reset-password")
    public ResponseEntity<Void> sendResetPassword(
            @Parameter(description = "Email of the user whose password needs to be reset", required = true,
                    schema = @Schema(type = "string")
            )
            @PathVariable String email) {
        log.info("Received request to reset password with email - {}.", email);
        userAuthService.sendResetPassword(email);
        log.info("Email send to {} for reset password", email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Reset user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Some data is missing or wrong",
                    content = {
                            @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                            @Schema(implementation = ErrorResponse.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Verification code not found", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                    @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequestDto requestDto,
                                              BindingResult bindingResult) {
        log.info("Received request to reset user password with Verification code.");
        userAuthService.resetPassword(requestDto, bindingResult);
        log.info("Password reset");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Get user by ID",
            description = "Get user details by user ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = SUCCESSFULLY_OPERATION),
                    @ApiResponse(responseCode = "404", description = NOT_FOUND),
                    @ApiResponse(responseCode = "500", description = SERVER_ERROR)
            },
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "User ID",
                            example = "1",
                            required = true
                    )
            }
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        userService.checkAccess(user);
        return userMapper.mapEntityToDto(user);
    }

    @Operation(summary = "Adding images to user")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponseDto uploadImages(@RequestParam("image") MultipartFile image) {
        User user = userService.getCurrentUser();
        userService.uploadImage(user, image);
        return userMapper.mapEntityToDto(user);
    }
}
