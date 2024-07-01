package org.petmarket.review.listener;

import jakarta.persistence.*;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.repository.AdvertisementRepository;
import org.petmarket.review.dto.AverageReview;
import org.petmarket.review.entity.Review;
import org.petmarket.review.repository.ReviewRepository;
import org.petmarket.users.entity.User;
import org.petmarket.users.repository.UserRepository;
import org.petmarket.utils.BeanUtils;

public class ReviewListener {
    @PrePersist
    public void updateRatingForNewReview(Review review) {
        ReviewRepository reviewRepository = BeanUtils.getBean(ReviewRepository.class);
        AdvertisementRepository advertisementRepository = BeanUtils.getBean(AdvertisementRepository.class);
        UserRepository userRepository = BeanUtils.getBean(UserRepository.class);

        Advertisement advertisement = review.getAdvertisement();
        User user = review.getUser();
        AverageReview advertisementAverageReview = reviewRepository
                .findAverageReviewByAdvertisementId(advertisement.getId());
        AverageReview userAverageReview = reviewRepository
                .findAverageReviewByUserId(review.getUser().getId());
        advertisement.setRating((advertisementAverageReview.getSum() + review.getValue())
                        / (advertisementAverageReview.getCount() + 1));
        user.setRating((userAverageReview.getSum() + review.getValue()) / (userAverageReview.getCount() + 1));

        advertisementRepository.save(advertisement);
        userRepository.save(user);
    }

    @PreRemove
    public void updateRatingForReviewRemoval(Review review) {
        ReviewRepository reviewRepository = BeanUtils.getBean(ReviewRepository.class);
        AdvertisementRepository advertisementRepository = BeanUtils.getBean(AdvertisementRepository.class);
        UserRepository userRepository = BeanUtils.getBean(UserRepository.class);

        Advertisement advertisement = review.getAdvertisement();
        User user = review.getUser();
        AverageReview advertisementAverageReview = reviewRepository
                .findAverageReviewByAdvertisementId(advertisement.getId());
        AverageReview userAverageReview = reviewRepository
                .findAverageReviewByUserId(review.getUser().getId());

        if (advertisementAverageReview.getCount() == 1) {
            advertisement.setRating(0);
        } else {
            advertisement.setRating((advertisementAverageReview.getSum() - review.getValue())
                    / (advertisementAverageReview.getCount() - 1));
        }

        if (userAverageReview.getCount() == 1) {
            user.setRating(0);
        } else {
            user.setRating((userAverageReview.getSum() - review.getValue()) / (userAverageReview.getCount() - 1));
        }

        advertisementRepository.save(advertisement);
        userRepository.save(user);
    }
}
