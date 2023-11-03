package org.petmarket.breeds.repository;

import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.breeds.entity.BreedComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreedCommentRepository extends JpaRepository<BreedComment, Long> {
    List<BreedComment> findByStatus(CommentStatus status, Pageable pageable);

}
