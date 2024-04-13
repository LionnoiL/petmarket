package org.petmarket.advertisements.images.repository;

import org.petmarket.advertisements.images.entity.AdvertisementImage;
import org.petmarket.advertisements.images.entity.AdvertisementImageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AdvertisementImageRepository extends JpaRepository<AdvertisementImage, Long> {
    @Query("""
            SELECT ai FROM AdvertisementImage ai
            JOIN ai.advertisement a
            WHERE a.status = 'DRAFT'
            AND a.updated < :deletionDate
            """)
    Page<AdvertisementImage> findImagesBeforeDeletionDate(
            @Param("deletionDate") LocalDateTime deletionDate,
            Pageable pageable);

    List<AdvertisementImage> findAllByAdvertisementId(Long advertisementId);

    List<AdvertisementImage> findAllByAdvertisementIdAndType(Long advertisementId, AdvertisementImageType type);
}
