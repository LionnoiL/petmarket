package org.petmarket.users.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.security.jwt.JwtUser;
import org.petmarket.users.dto.UserResponseDto;
import org.petmarket.users.entity.User;
import org.petmarket.users.mapper.UserMapper;
import org.petmarket.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Andriy Gaponov
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public static Long getCurrentUserId() {
        JwtUser principal = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return principal.getId();
    }

    public User findByUsername(String username) throws ItemNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    throw new ItemNotFoundException("User email not found");
                });
        log.info("IN findByUsername - user: {} found by username: {}", user, username);
        return user;
    }

    public User getCurrentUser() {
        try {
            JwtUser principal = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            return userRepository.findById(principal.getId()).orElseThrow(() -> {
                throw new ItemNotFoundException("User not found by id: " + principal.getId());
            });
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isCurrentUserAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public UserResponseDto findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new ItemNotFoundException("User not found by id: " + userId);
        });
        return userMapper.mapEntityToDto(user);
    }

    @Transactional
    public void setLastActivity(String username) {
        User user = findByUsername(username);
        if (user != null) {
            user.setLastActivity(LocalDateTime.now());
            userRepository.save(user);
        }
    }
}

