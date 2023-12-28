package org.petmarket.options.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.options.entity.Options;
import org.petmarket.options.entity.OptionsKey;
import org.petmarket.options.repository.OptionsRepository;
import org.springframework.stereotype.Service;

import static org.petmarket.utils.MessageUtils.LANGUAGE_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class OptionsService {

    private final OptionsRepository optionsRepository;
    private final LanguageRepository languageRepository;

    public Language getDefaultSiteLanguage() {
        Options options = optionsRepository.findByKey(OptionsKey.DEFAULT_LANGUAGE)
                .orElseThrow(() -> new ItemNotFoundException("Options not found"));
        return languageRepository.findByLangCodeAndEnableIsTrue(options.getValue())
                .orElseThrow(() -> new ItemNotFoundException(LANGUAGE_NOT_FOUND));
    }
}
