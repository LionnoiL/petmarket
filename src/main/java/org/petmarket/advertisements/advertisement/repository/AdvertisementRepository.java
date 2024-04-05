package org.petmarket.advertisements.advertisement.repository;

import jakarta.transaction.Transactional;
import org.petmarket.advertisements.advertisement.dto.AdvertisementPriceRangeDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.location.entity.City;
import org.petmarket.users.entity.UserStatus;
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

    @Query(
            value = """
                    SELECT a
                    FROM Advertisement a
                    JOIN a.author
                    WHERE a.category IN :categories
                    AND a.status = :status
                    AND a.author.status <> 'DELETED'
                    ORDER BY a.created DESC
                    """)
    Page<Advertisement> findAllByCategoryInAndStatusOrderByCreatedDesc(
            @Param("categories") List<AdvertisementCategory> categories,
            @Param("status") AdvertisementStatus status, Pageable pageable);

    Page<Advertisement> findAllByAuthorIdAndStatusAndIdNotOrderByCreatedDesc(Long authorId, AdvertisementStatus status,
                                                                             Long excludedAdvertisementId,
                                                                             Pageable pageable);

    @Query(
            value = """
                    SELECT a
                    FROM Advertisement a
                    JOIN a.author
                    WHERE a.status = :status
                    AND a.author.status <> 'DELETED'
                    ORDER BY a.created DESC
                    """)
    Page<Advertisement> findAllByStatusOrderByCreatedDesc(@Param("status") AdvertisementStatus status,
                                                          Pageable pageable);

    @Query(
            value = """
                    SELECT
                    new org.petmarket.advertisements.advertisement.dto.AdvertisementPriceRangeDto
                    (MIN(a.price), MAX(a.price))
                    FROM Advertisement a
                    JOIN a.author auth
                    JOIN a.category cat
                    WHERE cat.id = :categoryId
                    AND auth.status <> 'DELETED'
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

    @Query(value = """
            SELECT a.author.status
            FROM Advertisement a
            WHERE a.id = :advertisementId
            """)
    UserStatus getAdvertisementAuthorStatus(@Param("advertisementId") Long advertisementId);
}
