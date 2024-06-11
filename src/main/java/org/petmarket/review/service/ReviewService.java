package org.petmarket.review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.repository.AdvertisementRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.order.repository.OrderRepository;
import org.petmarket.review.dto.RatingList;
import org.petmarket.review.entity.Review;
import org.petmarket.review.repository.ReviewRepository;
import org.petmarket.users.entity.User;
import org.petmarket.users.repository.UserRepository;
import org.petmarket.users.service.UserCacheService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static org.petmarket.utils.MessageUtils.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AdvertisementRepository advertisementRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserCacheService userCacheService;

    public List<Review> findAllByUser(User user, int size) {
        if (user == null || size <= 0) {
            return Collections.emptyList();
        }
        return reviewRepository.findReviewByUserID(user.getId(), size);
    }

    public RatingList findRatingsByUser(User user) {
        if (user == null) {
            return null;
        }
        List<Integer[]> ratings = reviewRepository.findRatingsByUserID(user.getId());
        return new RatingList(ratings);
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = getReview(id);
        User user = review.getUser();
        reviewRepository.delete(review);
        userCacheService.evictCaches(user);
    }

    @Transactional
    public void deleteAllReviewsByAdvertisement(Long id) {
        if (!advertisementRepository.existsById(id)) {
            throw new ItemNotFoundException(ADVERTISEMENT_NOT_FOUND);
        }
        reviewRepository.deleteAllReviewsByAdvertisementId(id);
        userCacheService.evictCaches();
    }

    @Transactional
    public void deleteAllReviewsByUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ItemNotFoundException(USER_NOT_FOUND);
        }
        reviewRepository.deleteAllReviewsByUserId(id);
        userCacheService.evictCaches();
    }

    @Transactional
    public void deleteAllReviewsByOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ItemNotFoundException(ORDER_NOT_FOUND);
        }
        reviewRepository.deleteAllReviewsByOrderId(id);
        userCacheService.evictCaches();
    }

    private Review getReview(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(REVIEW_NOT_FOUND));
    }

    public boolean existsByAuthorIdAndUserId(Long authorId, Long userId) {
        return !reviewRepository.findReviewByAuthorIdAndUserId(authorId, userId).isEmpty();
    }
}
