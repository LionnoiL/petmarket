package org.petmarket.blog.repository;

import org.petmarket.blog.entity.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<BlogCategory, Long> {
}
