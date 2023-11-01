package org.petmarket.review.repository;

import org.petmarket.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepositoryBasic extends JpaRepository<Review, Long> {
}
