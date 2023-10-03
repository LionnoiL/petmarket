package org.petmarket.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.security.jwt.JwtUser;
import org.petmarket.users.entity.User;
import org.petmarket.users.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Andriy Gaponov
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findByUsername(String username) throws ItemNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    throw new ItemNotFoundException("User email not found");
                });
        log.info("IN findByUsername - user: {} found by username: {}", user, username);
        return user;
    }

    public Long getCurrentUserId() {
        JwtUser principal = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return principal.getId();
    }

    public boolean isCurrentUserAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return false;
    }
}
