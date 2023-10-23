package org.petmarket.advertisements.advertisement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementResponseDto;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementResponseTranslateMapper;
import org.petmarket.advertisements.advertisement.repository.AdvertisementRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.options.service.OptionsService;
import org.petmarket.utils.TransliterateUtils;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdvertisementService {

    public static final String LANGUAGE_NOT_FOUND_MESSAGE = "Language not found";
    public static final String ADVERTISEMENT_NOT_FOUND_MESSAGE = "Advertisement not found";

    private final LanguageRepository languageRepository;
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementResponseTranslateMapper translateMapper;

    private final OptionsService optionsService;
    private final TransliterateUtils transliterateUtils;

    private Language getLanguage(String langCode) {
        return languageRepository.findByLangCodeAndEnableIsTrue(langCode)
                .orElseThrow(() -> {
                    throw new ItemNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE);
                });
    }

    public AdvertisementResponseDto findById(Long id, String langCode) {
        Language language = getLanguage(langCode);

        return advertisementRepository.findById(id)
                .map(category -> translateMapper.mapEntityToDto(category, language))
                .orElseThrow(() -> new ItemNotFoundException(ADVERTISEMENT_NOT_FOUND_MESSAGE));
    }
}
