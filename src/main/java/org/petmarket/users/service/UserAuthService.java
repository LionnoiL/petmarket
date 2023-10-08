package org.petmarket.users.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotCreatedException;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.LoginException;
import org.petmarket.security.jwt.JwtResponseDto;
import org.petmarket.security.jwt.JwtTokenProvider;
import org.petmarket.security.jwt.JwtUser;
import org.petmarket.users.dto.UserRequestDto;
import org.petmarket.users.dto.UserResponseDto;
import org.petmarket.users.entity.LoginProvider;
import org.petmarket.users.entity.Role;
import org.petmarket.users.entity.User;
import org.petmarket.users.entity.UserStatus;
import org.petmarket.users.mapper.UserMapper;
import org.petmarket.users.repository.RoleRepository;
import org.petmarket.users.repository.UserRepository;
import org.petmarket.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAuthService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ErrorUtils errorUtils;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDto register(UserRequestDto userRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotCreatedException(errorUtils.getErrorsString(bindingResult));
        }

        User user = userMapper.mapDtoRequestToDto(userRequestDto);
        Role roleUser = roleRepository.findByName("ROLE_USER").orElseThrow(() -> {
            throw new ItemNotFoundException("Role not found");
        });
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(UserStatus.ACTIVE);
        user.setId(null);
        user.setLoginProvider(LoginProvider.LOCAL);

        User registeredUser = userRepository.save(user);

        return userMapper.mapEntityToDto(registeredUser);
    }

    public ResponseEntity<JwtResponseDto> login(UserRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new LoginException(errorUtils.getErrorsString(bindingResult));
        }

        try {
            String username = requestDto.getEmail();
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with email: " + username + " not found");
            }

            String accessToken = jwtTokenProvider.createToken(username, user.getRoles());
            String refreshToken = jwtTokenProvider.createRefreshToken(username, user.getRoles());

            JwtResponseDto response = new JwtResponseDto(username, accessToken, refreshToken);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    public ResponseEntity<JwtResponseDto> refreshToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token, false)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            JwtUser jwtUser = (JwtUser) auth.getPrincipal();
            User user = userRepository.findByEmail(jwtUser.getEmail()).get();
            String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(),
                user.getRoles());
            JwtResponseDto jwtResponseDto = new JwtResponseDto(
                user.getEmail(),accessToken, refreshToken
            );

            return ResponseEntity.ok(jwtResponseDto);
        }

        throw new BadCredentialsException("Invalid email or password");
    }
}
