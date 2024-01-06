package org.petmarket.advertisements.advertisement.repository;

import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
                        ORDER BY a.created
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
                        ORDER BY a.created
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

    Page<Advertisement> findAllByStatusOrderByCreatedDesc(AdvertisementStatus status, Pageable pageable);

    Page<Advertisement> findAdvertisementByStatusAndUpdatedBeforeAndImagesIsNotEmpty(AdvertisementStatus status,
                                                                                     LocalDateTime dateBefore,
                                                                                     Pageable pageable);
}
