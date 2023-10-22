package org.petmarket.location.repository;

import java.util.List;
import org.petmarket.location.entity.City;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends CityRepositoryBasic {

    List<City> findByNameContainingOrderByName(String name);
}
