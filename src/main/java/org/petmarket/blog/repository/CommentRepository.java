package org.petmarket.blog.repository;

import org.petmarket.blog.entity.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<BlogComment, Long> {
}
