package org.petmarket.review.repository;

import org.petmarket.review.entity.Review;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends ReviewRepositoryBasic {

    @Query(value = """
            SELECT r.* FROM reviews r
            LEFT JOIN advertisements a ON r.advertisement_id = a.id
            WHERE a.id = :id ORDER BY r.id DESC
            """, nativeQuery = true)
    List<Review> findReviewByAdvertisementID(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM reviews r where r.advertisement_id = :id", nativeQuery = true)
    void deleteAllReviewsByAdvertisementId(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM reviews r where r.user_id = :id", nativeQuery = true)
    void deleteAllReviewsByUserId(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM reviews r where r.order_id = :id", nativeQuery = true)
    void deleteAllReviewsByOrderId(@Param("id") Long id);
}
