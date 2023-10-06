package org.petmarket.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.petmarket.errorhandling.ErrorResponse;
import org.petmarket.security.jwt.JwtResponseDto;
import org.petmarket.users.dto.UserRequestDto;
import org.petmarket.users.service.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "the user authentication API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/auth/")
public class AuthenticationRestController {

    private final UserAuthService userAuthService;

    @Operation(summary = "Login in and returns the authentication token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = JwtResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "It indicates that the server can not or will " +
                    "not process the request due to an apparent client error",
                    content = {
                            @Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))
                    })
    })
    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody UserRequestDto requestDto, BindingResult bindingResult) {
        return userAuthService.login(requestDto, bindingResult);
    }

    @Operation(summary = "User Registration",
            description = "The user registration API creates a user account in application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "The User has already been added " +
                    "or some data is missing",
                    content = {
                            @Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))
                    })
    })
    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody UserRequestDto requestDto,
                                   BindingResult bindingResult) {
        userAuthService.register(requestDto, bindingResult);
        return ResponseEntity.ok().build();
    }
}
