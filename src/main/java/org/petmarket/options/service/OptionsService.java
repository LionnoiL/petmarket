package org.petmarket.options.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.options.dto.OptionsRequestDto;
import org.petmarket.options.entity.Options;
import org.petmarket.options.entity.OptionsKey;
import org.petmarket.options.repository.OptionsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.petmarket.utils.MessageUtils.LANGUAGE_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class OptionsService {

    private final OptionsRepository optionsRepository;
    private final LanguageRepository languageRepository;

    public Language getDefaultSiteLanguage() {
        String langCode = getOptionsValueByKey(OptionsKey.DEFAULT_LANGUAGE);
        return languageRepository.findByLangCodeAndEnableIsTrue(langCode)
                .orElseThrow(() -> new ItemNotFoundException(LANGUAGE_NOT_FOUND));
    }

    public Options getOptionsByKey(OptionsKey key) {
        return optionsRepository.findByKey(key)
                .orElseThrow(() -> new ItemNotFoundException("Option not found with key: " + key));
    }

    public String getOptionsValueByKey(OptionsKey key) {
        Options options = getOptionsByKey(key);
        return options.getValue();
    }

    public Options setOptionsValueByKey(OptionsKey key, OptionsRequestDto dto) {
        Options options = getOptionsByKey(key);
        options.setValue(dto.getValue());
        optionsRepository.save(options);
        return options;
    }

    public List<Long> getFavoriteAttributesGroupIds() {
        List<Long> result = new ArrayList<>();

        String optionsValueByKey = getOptionsValueByKey(OptionsKey.FAVORITE_ATTRIBUTE_1_ID);
        if (!Strings.isBlank(optionsValueByKey)) {
            result.add(Long.valueOf(optionsValueByKey));
        }

        optionsValueByKey = getOptionsValueByKey(OptionsKey.FAVORITE_ATTRIBUTE_2_ID);
        if (!Strings.isBlank(optionsValueByKey)) {
            result.add(Long.valueOf(optionsValueByKey));
        }
        return result;
    }
}
