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
import org.petmarket.security.jwt.AuthenticationRequestDto;
import org.petmarket.security.jwt.JwtResponseDto;
import org.petmarket.security.jwt.JwtTokenProvider;
import org.petmarket.users.dto.UserRequestDto;
import org.petmarket.users.entity.User;
import org.petmarket.users.service.UserAuthService;
import org.petmarket.users.service.UserService;
import org.petmarket.utils.ErrorUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;


@Tag(name = "Authentication", description = "the user authentication API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/auth/")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserAuthService userAuthService;
    private final ErrorUtils errorUtils;

    @Operation(summary = "Login in and returns the authentication token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = JwtResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "It indicates that the server can not or will not process the request due to an apparent client error",
                    content = {
                            @Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorResponse.class))
                    })
    })
    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody AuthenticationRequestDto requestDto, BindingResult bindingResult) throws LoginException {
        if (bindingResult.hasErrors()) {
            throw new LoginException(errorUtils.getErrorsString(bindingResult));
        }

        try {
            String username = requestDto.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with email: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            JwtResponseDto response = new JwtResponseDto(username, token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Operation(summary = "User Registration", description = "The user registration API creates a user account in application")
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
    public ResponseEntity register(@Valid @RequestBody UserRequestDto requestDto, BindingResult bindingResult) {
        userAuthService.register(requestDto, bindingResult);
        return ResponseEntity.ok().build();
    }
}
