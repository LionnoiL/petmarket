package org.petmarket.location.repository;

import org.petmarket.location.entity.Country;
import org.petmarket.location.entity.State;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends StateRepositoryBasic {

    List<State> findByNameContainingOrderByName(String name);

    Optional<State> findByKoatuuCode(String koatuu);

    List<State> findByCountryAndNameContainingOrderByName(Country country, String name);
}
