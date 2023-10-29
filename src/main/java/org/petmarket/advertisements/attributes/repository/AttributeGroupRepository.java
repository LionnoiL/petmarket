package org.petmarket.advertisements.attributes.repository;

import org.petmarket.advertisements.attributes.entity.AttributeGroup;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;

import java.util.List;

public interface AttributeGroupRepository extends AttributeGroupRepositoryBasic {

    List<AttributeGroup> findAllByOrderBySortValueAsc();

    List<AttributeGroup> findAllByCategoryOrderBySortValueAsc(AdvertisementCategory category);

    List<AttributeGroup> findAllByUseInFilterOrderBySortValueAsc(boolean useInFilter);
}
