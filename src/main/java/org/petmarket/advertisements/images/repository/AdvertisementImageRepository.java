package org.petmarket.advertisements.images.repository;

import org.petmarket.advertisements.images.entity.AdvertisementImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementImageRepository extends JpaRepository<AdvertisementImage, Long> {
}
