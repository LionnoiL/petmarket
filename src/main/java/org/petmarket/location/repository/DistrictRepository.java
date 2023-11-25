package org.petmarket.location.repository;

import org.petmarket.location.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Long> {

    Optional<District> findByKoatuuCode(String koatuu);
}
