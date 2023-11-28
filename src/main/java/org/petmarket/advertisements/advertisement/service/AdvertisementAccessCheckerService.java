package org.petmarket.advertisements.advertisement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.users.entity.User;
import org.petmarket.users.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdvertisementAccessCheckerService {

    private final UserService userService;

    public void checkAccess(List<Advertisement> advertisements) {
        if (!userService.isCurrentUserAdmin()) {
            User user = userService.getCurrentUser();
            for (Advertisement advertisement : advertisements) {
                if (!advertisement.getAuthor().equals(user)) {
                    throw new AccessDeniedException("Access denied to advertisement with id " + advertisement.getId());
                }
            }
        }
    }
}
