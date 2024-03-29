package org.petmarket.blog.repository;

import org.petmarket.blog.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT p.* FROM blog_posts p " +
            "INNER JOIN post_categories pc ON p.id = pc.post_id " +
            "WHERE pc.category_id = :categoryId", nativeQuery = true)
    List<Post> findPostsByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query(value = """
            SELECT p.* FROM blog_posts p
            INNER JOIN posts_attributes pa ON p.id = pa.post_id
            INNER JOIN blog_attributes a ON pa.attribute_id = a.id
            WHERE a.id = :attributeId
            """, nativeQuery = true)
    List<Post> findPostsByAttributeId(@Param("attributeId") Long attributeId, Pageable pageable);
}
