package org.petmarket.review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.repository.AdvertisementRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.order.repository.OrderRepository;
import org.petmarket.review.entity.Review;
import org.petmarket.review.repository.ReviewRepository;
import org.petmarket.users.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private static final String REVIEW_NOT_FOUND_MESSAGE = "Review not found";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String ADVERTISEMENT_NOT_FOUND_MESSAGE = "Advertisement not found";
    private static final String ORDER_NOT_FOUND_MESSAGE = "Order not found";
    private final ReviewRepository reviewRepository;
    private final AdvertisementRepository advertisementRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public void deleteReview(Long id) {
        Review review = getReview(id);
        reviewRepository.delete(review);
    }

    @Transactional
    public void deleteAllReviewsByAdvertisement(Long id) {
        if (!advertisementRepository.existsById(id)) {
            throw new ItemNotFoundException(ADVERTISEMENT_NOT_FOUND_MESSAGE);
        }
        reviewRepository.deleteAllReviewsByAdvertisementId(id);
    }

    @Transactional
    public void deleteAllReviewsByUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ItemNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        reviewRepository.deleteAllReviewsByUserId(id);
    }

    @Transactional
    public void deleteAllReviewsByOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ItemNotFoundException(ORDER_NOT_FOUND_MESSAGE);
        }
        reviewRepository.deleteAllReviewsByOrderId(id);
    }

    private Review getReview(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(REVIEW_NOT_FOUND_MESSAGE);
        });
    }
}
