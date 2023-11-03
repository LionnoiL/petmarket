package org.petmarket.location.repository;

import java.util.List;
import org.petmarket.location.entity.Country;
import org.petmarket.location.entity.State;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends StateRepositoryBasic {

    List<State> findByNameContainingOrderByName(String name);

    List<State> findByCountryAndNameContainingOrderByName(Country country, String name);
}
