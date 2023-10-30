package org.petmarket.advertisements.attributes.repository;

import org.petmarket.advertisements.attributes.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepositoryBasic extends JpaRepository<Attribute, Long> {
}
