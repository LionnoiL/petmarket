package org.petmarket.location.repository;

import java.util.List;
import org.petmarket.location.entity.Country;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CountryRepositoryBasic {

    List<Country> findByNameContainingOrderByName(String name);
}
