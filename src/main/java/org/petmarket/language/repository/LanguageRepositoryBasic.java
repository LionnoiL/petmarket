package org.petmarket.language.repository;

import org.petmarket.language.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepositoryBasic extends JpaRepository<Language, String> {
}
