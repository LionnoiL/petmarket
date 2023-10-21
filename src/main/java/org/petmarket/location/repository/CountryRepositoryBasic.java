package org.petmarket.location.repository;

import org.petmarket.location.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepositoryBasic extends JpaRepository<Country, Long> {

}
