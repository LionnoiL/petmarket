package org.petmarket.advertisements.advertisement.repository;

import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends AdvertisementRepositoryBasic {

    List<Advertisement> findTop1000ByStatusEqualsOrderByCreatedDesc(AdvertisementStatus status);
}
