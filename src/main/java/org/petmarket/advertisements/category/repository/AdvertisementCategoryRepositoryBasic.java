package org.petmarket.advertisements.category.repository;

import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementCategoryRepositoryBasic extends JpaRepository<AdvertisementCategory, Long> {
}
