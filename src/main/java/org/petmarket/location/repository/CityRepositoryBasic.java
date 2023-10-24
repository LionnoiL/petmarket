package org.petmarket.location.repository;

import org.petmarket.location.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepositoryBasic extends JpaRepository<City, Long> {

}
