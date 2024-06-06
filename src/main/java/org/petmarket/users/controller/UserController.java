package org.petmarket.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.security.front.FrontTokenRequestDto;
import org.petmarket.security.front.FrontTokenService;
import org.petmarket.users.dto.*;
import org.petmarket.users.entity.User;
import org.petmarket.users.mapper.UserMapper;
import org.petmarket.users.service.UserAuthService;
import org.petmarket.users.service.UserService;
import org.petmarket.utils.ErrorUtils;
import org.petmarket.utils.annotations.parametrs.ParameterId;
import org.petmarket.utils.annotations.parametrs.ParameterPageNumber;
import org.petmarket.utils.annotations.parametrs.ParameterPageSize;
import org.petmarket.utils.annotations.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private final FrontTokenService frontTokenService;
    private final AdvertisementService advertisementService;
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
    public ResponseEntity<Void> resetPassword(
            @RequestBody @Valid ResetPasswordRequestDto requestDto,
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

    @Operation(summary = "Get user contacts")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseNotFound
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{userId}/contacts")
    public UserContactsResponseDto getContacts(@PathVariable Long userId,
                                               @RequestBody @Valid FrontTokenRequestDto tokenRequestDto) {
        log.info("Received request to get users contacts");
        frontTokenService.checkToken(tokenRequestDto);
        User user = userService.findById(userId);
        return userService.getContacts(user);
    }

    @Operation(summary = "Add advertisement to favorite",
            description = """
                    If the advertisement is not in the list of favorites, it is added. Otherwise, it is deleted.
                    """)
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseNotFound
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/advertisements/favorite/{advertisementId}")
    public ResponseEntity<Boolean> addOrDeleteAdvertisementToFavorite(@PathVariable Long advertisementId, Authentication authentication) {
        log.info("Received request to add advertisement to favorite list");
        User user = userService.findByUsername(authentication.getName());
        Advertisement advertisement = advertisementService.getAdvertisement(advertisementId);
        boolean advertisementInList = userService.addOrDeleteAdvertisementToFavorite(user, advertisement);
        return ResponseEntity.ok(advertisementInList);
    }

    @Operation(summary = "Update User by ID")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{userId}")
    public UserResponseDto updateUser(
            @ParameterId @PathVariable @Positive Long userId,
            @RequestBody @Valid @NotNull(message = REQUEST_BODY_IS_MANDATORY) final UserUpdateRequestDto request,
            BindingResult bindingResult) {
        ErrorUtils.checkItemNotUpdatedException(bindingResult);
        log.debug("Received request to update User - {} with id {}.", request, userId);
        User user = userService.findById(userId);
        userService.checkAccess(user);
        return userMapper.mapEntityToDto(userService.updateUser(user, request));
    }

    @Operation(summary = "Update user password")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseNotFound
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update-password")
    public void updatePassword(@RequestBody @Valid UpdatePasswordRequestDto updatePasswordRequestDto) {
        User user = userService.findByUsername(updatePasswordRequestDto.getEmail());
        userService.checkAccess(user);
        userAuthService.updatePassword(user, updatePasswordRequestDto.getNewPassword());
        log.info("Password changed for user with email: " + updatePasswordRequestDto.getEmail());
    }

    @Operation(summary = "Delete user by ID")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    @ApiResponseForbidden
    @ApiResponseNotFound
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{userId}")
    public void deleteUser(@ParameterId @PathVariable @Positive Long userId) {
        log.info("Received request to delete User with id {}.", userId);
        User user = userService.findById(userId);
        userService.checkAccess(user);
        userService.deleteById(userId);
        log.info("User with id {} deleted.", userId);
    }

    @Operation(summary = "Add user to black list")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/blacklist/{userId}")
    public void addToBlackList(@ParameterId @PathVariable @Positive Long userId) {
        userService.addUserToBlacklist(userId);
        log.info("User with id {} added to black list.", userId);
    }

    @Operation(summary = "Remove user from black list")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @ApiResponseNotFound
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/blacklist/{userId}")
    public void removeFromBlackList(@ParameterId @PathVariable @Positive Long userId) {
        userService.removeUserFromBlacklist(userId);
        log.info("User with id {} removed from black list.", userId);
    }

    @Operation(summary = "Get black list")
    @ApiResponseSuccessful
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/blacklist")
    public List<BlackListResponseDto> getBlackList(
            @ParameterPageNumber @RequestParam(defaultValue = "1") @Positive int page,
            @ParameterPageSize @RequestParam(defaultValue = "30") @Positive int size) {
        return userService.getBlackList(page, size);
    }
}
