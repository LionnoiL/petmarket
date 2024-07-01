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

    @Query(value = """
            SELECT r.* FROM reviews r
            WHERE r.user_id = :id ORDER BY r.id DESC
            LIMIT :size
            """, nativeQuery = true)
    List<Review> findReviewByUserID(@Param("id") Long id, @Param("size") int size);

    @Query(value = """
            SELECT
               SUM(CASE WHEN review_value = 1 THEN 1 ELSE 0 END) AS rating1,
               SUM(CASE WHEN review_value = 2 THEN 1 ELSE 0 END) AS rating2,
               SUM(CASE WHEN review_value = 3 THEN 1 ELSE 0 END) AS rating3,
               SUM(CASE WHEN review_value = 4 THEN 1 ELSE 0 END) AS rating4,
               SUM(CASE WHEN review_value = 5 THEN 1 ELSE 0 END) AS rating5
            FROM reviews
                WHERE user_id = :id
            """, nativeQuery = true)
    List<Integer[]> findRatingsByUserID(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM reviews r where r.advertisement_id = :id", nativeQuery = true)
    void deleteAllReviewsByAdvertisementId(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM reviews r where r.user_id = :id", nativeQuery = true)
    void deleteAllReviewsByUserId(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM reviews r where r.order_id = :id", nativeQuery = true)
    void deleteAllReviewsByOrderId(@Param("id") Long id);

    @Query(value = """
            SELECT r.* FROM reviews r
            WHERE r.author_id = :authorId AND r.user_id = :userId
            """, nativeQuery = true)
    List<Review> findReviewByAuthorIdAndUserId(@Param("authorId") Long authorId, @Param("userId") Long userId);
}
