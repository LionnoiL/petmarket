package org.petmarket.breeds.repository;

import org.petmarket.blog.entity.CommentStatus;
import org.petmarket.breeds.entity.BreedComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreedCommentRepository extends JpaRepository<BreedComment, Long> {
    List<BreedComment> findByStatus(CommentStatus status, Pageable pageable);

    @Query(value = "SELECT bc " +
            "FROM BreedComment bc " +
            "JOIN bc.breed b " +
            "WHERE b.id = :breedId " +
            "AND bc.status = :status")
    List<BreedComment> findAllByStatusAndBreedId(@Param("breedId") Long breedId,
                                                 @Param("status") CommentStatus status,
                                                 Pageable pageable);

    @Query("SELECT bc " +
            "FROM BreedComment bc " +
            "JOIN bc.user u " +
            "JOIN bc.breed b " +
            "WHERE u.username = :username " +
            "AND b.id = :breedId " +
            "AND bc.status = :status")
    List<BreedComment> findAllByUsernameAndBreedId(@Param("username") String username,
                                                   @Param("breedId") Long breedId,
                                                   @Param("status") CommentStatus status);

}
