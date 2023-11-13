package org.petmarket.location.repository;

import org.petmarket.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepositoryBasic extends JpaRepository<Location, Long> {
}
