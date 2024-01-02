package org.petmarket.users.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.files.FileStorageName;
import org.petmarket.images.ImageService;
import org.petmarket.security.jwt.JwtUser;
import org.petmarket.users.entity.User;
import org.petmarket.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    @Value("${aws.s3.catalog.users}")
    private String catalogName;
    @Value("${users.images.avatar.width}")
    private int imageWidth;
    @Value("${users.images.avatar.height}")
    private int imageHeight;

    private final UserRepository userRepository;
    private final ImageService imageService;

    public static Long getCurrentUserId() {
        JwtUser principal = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return principal.getId();
    }

    public User findByUsername(String username) throws ItemNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ItemNotFoundException("User email not found"));
        log.info("IN findByUsername - user: {} found by username: {}", user, username);
        return user;
    }

    public User getCurrentUser() {
        try {
            JwtUser principal = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            return userRepository.findById(principal.getId())
                    .orElseThrow(() -> new ItemNotFoundException("User not found by id: " + principal.getId()));
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isCurrentUserAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ItemNotFoundException("User not found by id: " + userId));
    }

    @Transactional
    public void setLastActivity(String username) {
        User user = findByUsername(username);
        if (user != null) {
            user.setLastActivity(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Transactional
    public void uploadImage(User user, MultipartFile image) {
        FileStorageName storage = imageService.convertAndSendImage(catalogName, user.getId(),
                image, imageWidth, imageHeight, "avatar");
        String oldAvatarUrl = user.getUserAvatarUrl();
        user.setUserAvatarUrl(storage.getFullName());
        userRepository.save(user);
        imageService.deleteImage(catalogName, oldAvatarUrl);
    }
}
