package org.petmarket.options.repository;

import org.petmarket.options.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionsRepositoryBasic extends JpaRepository<Options, Long> {
}
