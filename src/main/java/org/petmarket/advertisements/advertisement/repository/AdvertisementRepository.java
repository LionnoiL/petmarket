package org.petmarket.advertisements.advertisement.repository;

import jakarta.transaction.Transactional;
import org.petmarket.advertisements.advertisement.dto.AdvertisementPriceRangeDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.location.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends AdvertisementRepositoryBasic {

    List<Advertisement> findTop1000ByStatusEqualsOrderByCreatedDesc(AdvertisementStatus status);

    @Query(
            value = """
                    SELECT r.id FROM
                    (SELECT c.category_id id, COUNT(c.id) cc FROM (
                        SELECT a.category_id, a.id
                        FROM advertisements a
                        WHERE a.advertisement_status = 'ACTIVE'
                        ORDER BY a.created desc
                        LIMIT 1000
                    ) c
                    GROUP BY c.category_id
                    ORDER BY cc DESC) r
                    LEFT JOIN categories cat ON cat.id = r.id
                    WHERE cat.available_in_favorite = 1
                    LIMIT :limit
                    """,
            nativeQuery = true)
    List<Long> findFavoriteCategories(@Param("limit") int limit);

    @Query(
            value = """
                    SELECT r.id FROM
                    (SELECT c.category_id id, COUNT(c.id) cc FROM (
                        SELECT a.category_id, a.id
                        FROM advertisements a
                        WHERE a.advertisement_status = 'ACTIVE'
                        ORDER BY a.created desc
                        LIMIT 1000
                    ) c
                    GROUP BY c.category_id
                    ORDER BY cc DESC) r
                    LEFT JOIN categories cat ON cat.id = r.id
                    WHERE cat.available_in_tags = 1
                    LIMIT :limit
                    """,
            nativeQuery = true)
    List<Long> findFavoriteTags(@Param("limit") int limit);

    Page<Advertisement> findAllByCategoryInAndStatusOrderByCreatedDesc(
            List<AdvertisementCategory> categories, AdvertisementStatus status, Pageable pageable);

    Page<Advertisement> findAllByAuthorIdAndStatusAndIdNotOrderByCreatedDesc(Long authorId, AdvertisementStatus status,
                                                                             Long excludedAdvertisementId,
                                                                             Pageable pageable);

    Page<Advertisement> findAllByStatusOrderByCreatedDesc(AdvertisementStatus status,
                                                          Pageable pageable);

    @Query(
            value = """
              SELECT
              new org.petmarket.advertisements.advertisement.dto.AdvertisementPriceRangeDto(MIN(a.price), MAX(a.price))
              FROM Advertisement a
              WHERE a.category.id = :categoryId
              AND a.status = 'ACTIVE'
                    """)
    AdvertisementPriceRangeDto getAdvertisementPriceRangeByCategory(@Param("categoryId") Long categoryId);

    @Query(value = """
            SELECT b, COUNT(adv.id)
              FROM Advertisement adv
              JOIN adv.breed b
              WHERE adv.category = :category AND adv.status = 'ACTIVE'
              GROUP BY b
            """)
    List<Object[]> findAllBreedsByCategoryId(@Param("category") AdvertisementCategory category);

    @Query(value = """
            SELECT DISTINCT c
            FROM Advertisement adv
            JOIN adv.location l
            JOIN l.city c
            WHERE adv.category = :category AND adv.status = 'ACTIVE'
            """)
    List<City> findAllCitiesByCategoryId(@Param("category") AdvertisementCategory category);

    @Transactional
    @Modifying
    @Query(value = """
            update Advertisement adv
            set adv.status = :newStatus
            WHERE adv.status = :oldStatus
            """)
    void updateStatus(@Param("oldStatus") AdvertisementStatus oldStatus,
                      @Param("newStatus") AdvertisementStatus newStatus
    );
}
