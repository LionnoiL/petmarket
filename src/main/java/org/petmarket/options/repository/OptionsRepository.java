package org.petmarket.options.repository;

import org.petmarket.options.entity.Options;
import org.petmarket.options.entity.OptionsKey;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionsRepository extends OptionsRepositoryBasic {

    Optional<Options> findByKey(OptionsKey key);
}
