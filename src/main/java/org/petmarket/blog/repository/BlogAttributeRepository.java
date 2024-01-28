package org.petmarket.blog.repository;

import org.petmarket.blog.entity.BlogAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogAttributeRepository extends JpaRepository<BlogAttribute, Long> {
}
