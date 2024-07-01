package org.petmarket.advertisements.advertisement.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.transaction.Transactional;
import org.petmarket.advertisements.advertisement.entity.Advertisement;

import static org.petmarket.advertisements.advertisement.service.AdvertisementService.calculateRating;

@Transactional
public class AdvertisementListener {
    @PrePersist
    @PreUpdate
    public void updateTopRating(Advertisement advertisement) {
        advertisement.setTopRating(calculateRating(advertisement));
    }
}
