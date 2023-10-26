package org.petmarket.breeds.repository;

import org.petmarket.breeds.entity.BreedComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreedCommentRepository extends JpaRepository<BreedComment, Long> {
}
