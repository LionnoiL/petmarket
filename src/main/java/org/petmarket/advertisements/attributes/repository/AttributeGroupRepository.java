package org.petmarket.advertisements.attributes.repository;

import java.util.List;
import org.petmarket.advertisements.attributes.entity.AttributeGroup;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttributeGroupRepository extends AttributeGroupRepositoryBasic {

    List<AttributeGroup> findAllByOrderBySortValueAsc();

    @Query("SELECT ag FROM AttributeGroup ag JOIN ag.categories ac WHERE ac = :category ORDER BY ag.created ASC")
    List<AttributeGroup> findAllByCategoryOrderBySortValueAsc(
        @Param("category") AdvertisementCategory category);

    List<AttributeGroup> findAllByUseInFilterOrderBySortValueAsc(boolean useInFilter);
}
