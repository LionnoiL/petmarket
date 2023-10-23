package org.petmarket.advertisements.advertisement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.repository.AdvertisementRepository;
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

    private final OptionsService optionsService;
    private final TransliterateUtils transliterateUtils;
}
