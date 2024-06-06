package org.petmarket.users.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.errorhandling.AccessDeniedException;
import org.petmarket.errorhandling.AdvertisementStatusException;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.files.FileStorageName;
import org.petmarket.images.ImageService;
import org.petmarket.notifications.NotificationService;
import org.petmarket.notifications.NotificationType;
import org.petmarket.review.dto.RatingList;
import org.petmarket.review.dto.UserReviewListResponseDto;
import org.petmarket.review.entity.Review;
import org.petmarket.review.mapper.ReviewMapper;
import org.petmarket.review.service.ReviewService;
import org.petmarket.security.jwt.JwtUser;
import org.petmarket.users.dto.BlackListResponseDto;
import org.petmarket.users.dto.UserContactsResponseDto;
import org.petmarket.users.dto.UserUpdateRequestDto;
import org.petmarket.users.entity.BlackList;
import org.petmarket.users.entity.FavoriteAdvertisement;
import org.petmarket.users.entity.User;
import org.petmarket.users.entity.UserPhone;
import org.petmarket.users.mapper.BlackListMapper;
import org.petmarket.users.repository.BlackListRepository;
import org.petmarket.users.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static org.petmarket.utils.MessageUtils.ACCESS_DENIED;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;
    private final ReviewService reviewService;
    private final NotificationService emailService;
    private final ReviewMapper reviewMapper;
    private final BlackListRepository blackListRepository;
    private final BlackListMapper blackListMapper;

    @Value("${aws.s3.catalog.users}")
    private String catalogName;
    @Value("${users.images.avatar.width}")
    private int imageWidth;
    @Value("${users.images.avatar.height}")
    private int imageHeight;

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
        imageService.deleteImageFromS3(catalogName, oldAvatarUrl);
    }

    public void checkAccess(User chekedUser) {
        if (!isCurrentUserAdmin()) {
            User user = getCurrentUser();
            if (user == null || !user.equals(chekedUser)) {
                throw new AccessDeniedException(String.format(ACCESS_DENIED, "user", chekedUser.getId()));
            }
        }
    }

    public UserContactsResponseDto getContacts(User user) {
        UserContactsResponseDto contacts = new UserContactsResponseDto();
        if (user == null) {
            return contacts;
        }

        contacts.setUserId(user.getId());
        contacts.setEmail(user.getEmail());
        contacts.setInstagramLink(user.getInstagramLink());
        contacts.setTwitterLink(user.getTwitterLink());
        contacts.setFacebookLink(user.getFacebookLink());

        Set<UserPhone> phones = user.getPhones();
        Set<String> phonesDto = new HashSet<>();
        if (phones != null) {
            for (UserPhone userPhone : phones) {
                phonesDto.add(userPhone.getPhoneNumber());
            }

            Optional<UserPhone> mainPhone = phones.stream().filter(UserPhone::getMain).findFirst();
            if (mainPhone.isPresent()) {
                contacts.setMainPhone(mainPhone.get().getPhoneNumber());
            } else {
                contacts.setMainPhone("");
            }
        }
        contacts.setPhones(phonesDto);
        return contacts;
    }

    @Transactional
    public User updateUser(User user, UserUpdateRequestDto request) {
        String oldUserEmail = user.getEmail();

        BeanUtils.copyProperties(request, user);
        user.setUsername(user.getEmail());
        user.setPhones(mergePhones(user, request));
        userRepository.save(user);

        if (!Objects.equals(oldUserEmail, user.getEmail())) {
            Map<String, Object> fields = new HashMap<>();
            emailService.send(NotificationType.MAIL_UPDATE_SUCCESSFULLY, fields, user);
            emailService.send(NotificationType.MAIL_UPDATE_SUCCESSFULLY, fields, user.getLanguage(), oldUserEmail);
        }

        return user;
    }

    @Transactional
    public void deleteById(Long userId) {
        userRepository.setUserStatusToDeleted(userId);
    }

    private Set<UserPhone> mergePhones(User user, UserUpdateRequestDto request) {
        Set<String> newPhones = request.getPhones();
        String mainPhone = request.getMainPhone();
        if (mainPhone != null && !mainPhone.isBlank()) {
            newPhones.add(mainPhone);
        }
        Set<UserPhone> phones = user.getPhones();
        if (phones == null) {
            phones = new HashSet<>();
        }

        phones.removeIf(phone -> !newPhones.contains(phone.getPhoneNumber()));

        Set<UserPhone> finalPhones = phones;
        newPhones.stream()
                .filter(phoneNumber -> finalPhones.stream()
                        .noneMatch(phone -> phone.getPhoneNumber().equals(phoneNumber)))
                .forEach(phoneNumber -> finalPhones.add(UserPhone.builder()
                        .user(user)
                        .phoneNumber(phoneNumber)
                        .build()));

        phones.stream()
                .forEach(phone -> phone.setMain(Objects.equals(phone.getPhoneNumber(), request.getMainPhone())));
        return phones;
    }

    public UserReviewListResponseDto getReviewsByUser(User user, int size) {
        if (user == null || size <= 0) {
            return null;
        }
        List<Review> reviews = reviewService.findAllByUser(user, size);
        RatingList ratingList = reviewService.findRatingsByUser(user);

        return UserReviewListResponseDto.builder()
                .reviewsCount(user.getReviewsCount())
                .rating(user.getRating())
                .ratingList(ratingList)
                .reviews(reviewMapper.mapEntityToUserReviewDto(reviews))
                .build();
    }

    @Transactional
    public boolean addOrDeleteAdvertisementToFavorite(User user, Advertisement advertisement) {
        if (user == null) {
            throw new ItemNotFoundException("User not found");
        }
        if (advertisement == null) {
            throw new ItemNotFoundException("Advertisement not found");
        }
        boolean advertisementInList = false;
        List<FavoriteAdvertisement> favoriteAdvertisements = user.getFavoriteAdvertisements();
        boolean exist = favoriteAdvertisements.stream().anyMatch(a -> a.getAdvertisement().equals(advertisement));
        if (exist) {
            favoriteAdvertisements.removeIf(a -> a.getAdvertisement().equals(advertisement));
        } else {
            if (!AdvertisementStatus.ACTIVE.equals(advertisement.getStatus())) {
                throw new AdvertisementStatusException("Advertisement not active");
            }
            favoriteAdvertisements.add(FavoriteAdvertisement.builder()
                    .advertisement(advertisement)
                    .user(user)
                    .build());
            advertisementInList = true;
        }
        userRepository.save(user);
        return advertisementInList;
    }

    public Boolean isUserBlacklisted(Long ownerId, Long userId) {
        return userRepository.countBlacklistedUsers(ownerId, userId) > 0;
    }

    public void addUserToBlacklist(Long userId) {
        Long ownerId = getCurrentUserId();

        if (isUserBlacklisted(ownerId, userId)) {
            return;
        }

        blackListRepository.save(BlackList.builder()
                .owner(findById(ownerId))
                .user(findById(userId))
                .build()
        );
    }

    public void removeUserFromBlacklist(Long userId) {
        blackListRepository.delete(blackListRepository.findByOwnerIdAndUserId(getCurrentUserId(), userId)
                .orElseThrow(() -> new ItemNotFoundException("User not found in blacklist")));
    }

    public List<BlackListResponseDto> getBlackList(int page, int size) {
        return blackListMapper.mapEntityToBlackListDto(userRepository
                .findBlackListedUsersByOwnerId(getCurrentUserId(), PageRequest.of(page - 1, size)).getContent());
    }
}
