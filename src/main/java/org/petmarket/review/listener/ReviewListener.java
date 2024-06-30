package org.petmarket.review.listener;

import jakarta.persistence.*;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.review.entity.Review;
import org.springframework.context.annotation.Lazy;

public class ReviewListener {
    private final AdvertisementService advertisementService;

    public ReviewListener(@Lazy AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    public void updateRating(Review review) {
        advertisementService.updateRating(review);
        advertisementService.updateTopRating(review.getAdvertisement());
    }
}
