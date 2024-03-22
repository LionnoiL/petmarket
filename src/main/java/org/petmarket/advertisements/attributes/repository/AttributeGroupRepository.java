package org.petmarket.advertisements.attributes.repository;

import org.petmarket.advertisements.attributes.entity.AttributeGroup;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttributeGroupRepository extends AttributeGroupRepositoryBasic {

    List<AttributeGroup> findAllByOrderBySortValueAsc();

    @Query("""
                SELECT ag FROM AttributeGroup ag JOIN ag.categories ac
                WHERE ag.useInFilter = :useInFilter AND ac = :category
                ORDER BY ag.created ASC
            """)
    List<AttributeGroup> findAllByCategoryAndUseInFilter(
            @Param("category") AdvertisementCategory category, @Param("useInFilter") boolean useInFilter);

    List<AttributeGroup> findAllByUseInFilterOrderBySortValueAsc(boolean useInFilter);

    @Query(value = """
            SELECT ag.* FROM attribute_groups ag
            Left JOIN attributes_group_categories ac ON ac.attribute_group_id = ag.id
            WHERE ac.category_id = :categoryId
            ORDER BY RAND() LIMIT :count
            """, nativeQuery = true)
    List<AttributeGroup> findRandomEntity(Long categoryId, int count);
}
