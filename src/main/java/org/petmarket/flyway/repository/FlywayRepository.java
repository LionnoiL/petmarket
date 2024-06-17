package org.petmarket.flyway.repository;

import org.petmarket.flyway.entity.FlywayHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlywayRepository extends JpaRepository<FlywayHistory, Long> {
}
