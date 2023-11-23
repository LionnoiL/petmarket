package org.petmarket.location.repository;

import org.petmarket.location.entity.City;
import org.petmarket.location.entity.State;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends CityRepositoryBasic {

    List<City> findByNameContainingOrderByName(String name, Pageable pageable);

    Optional<City> findByKoatuuCode(String koatuu);

    List<City> findByStateAndNameContainingOrderByName(State state, String name);
}
