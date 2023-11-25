package org.petmarket.advertisements.advertisement.repository;

import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdvertisementRepositoryBasic extends JpaRepository<Advertisement, Long>,
    JpaSpecificationExecutor {
}
