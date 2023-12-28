package org.petmarket.language.repository;

import org.petmarket.language.entity.Language;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends LanguageRepositoryBasic {

    Optional<Language> findByLangCodeAndEnableIsTrue(String langCode);

    List<Language> findAllByEnableIsTrue();
}
