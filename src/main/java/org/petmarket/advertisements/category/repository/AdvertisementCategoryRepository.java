package org.petmarket.advertisements.category.repository;

import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementCategoryRepository extends AdvertisementCategoryRepositoryBasic {

    @Query(value = "SELECT * FROM categories ORDER BY RAND() LIMIT 1", nativeQuery = true)
    AdvertisementCategory findRandomEntity();
}
