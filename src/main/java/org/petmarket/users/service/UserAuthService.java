package org.petmarket.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.users.converter.UserDtoMapper;
import org.petmarket.users.dto.UserRequestDto;
import org.petmarket.users.dto.UserResponseDto;
import org.petmarket.users.entity.Role;
import org.petmarket.users.entity.User;
import org.petmarket.users.entity.UserStatus;
import org.petmarket.users.repository.RoleRepository;
import org.petmarket.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAuthService {

    private final UserDtoMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDto register(UserRequestDto userRequestDto, BindingResult bindingResult) {

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

        User registeredUser = userRepository.save(user);

        return userMapper.mapEntityToDto(registeredUser);
    }
}
