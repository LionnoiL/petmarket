package org.petmarket.language.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotCreatedException;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.dto.LanguageCreateRequestDto;
import org.petmarket.language.dto.LanguageResponseDto;
import org.petmarket.language.dto.LanguageUpdateRequestDto;
import org.petmarket.language.entity.Language;
import org.petmarket.language.mapper.LanguageMapper;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.options.service.OptionsService;
import org.petmarket.utils.ErrorUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collection;
import java.util.List;

import static org.petmarket.utils.MessageUtils.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final OptionsService optionsService;
    private final LanguageMapper languageMapper;

    public Collection<LanguageResponseDto> getAll() {
        List<Language> languages = languageRepository.findAll();
        return languageMapper.mapEntityToDto(languages);
    }

    public Language getByLangCode(String langCode) {
        return languageRepository.findByLangCodeAndEnableIsTrue(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException(LANGUAGE_NOT_FOUND);
        });
    }

    @Transactional
    public LanguageResponseDto enableLanguage(String langCode) {
        Language language = getLanguage(langCode);
        language.setEnable(true);
        languageRepository.save(language);
        return languageMapper.mapEntityToDto(language);
    }

    @Transactional
    public LanguageResponseDto disableLanguage(String langCode) {
        Language language = getLanguage(langCode);
        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();

        if (language.equals(defaultSiteLanguage)) {
            throw new ItemNotUpdatedException(LANGUAGE_IS_DEFAULT);
        }

        language.setEnable(false);
        languageRepository.save(language);
        return languageMapper.mapEntityToDto(language);
    }

    @Transactional
    public LanguageResponseDto addLanguage(LanguageCreateRequestDto dto, BindingResult bindingResult) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);
        languageRepository.findById(dto.getLangCode())
                .ifPresent(language -> {
                    throw new ItemNotCreatedException(LANGUAGE_IS_ALREADY_IN_DATABASE);
                });

        Language language = languageMapper.mapDtoRequestToEntity(dto);
        languageRepository.save(language);
        log.info("The Language was created");
        return languageMapper.mapEntityToDto(language);
    }

    @Transactional
    public LanguageResponseDto updateLanguage(String langCode, LanguageUpdateRequestDto dto,
                                              BindingResult bindingResult) {
        ErrorUtils.checkItemNotUpdatedException(bindingResult);

        Language language = getLanguage(langCode);
        language.setName(dto.getName());
        language.setEnable(dto.getEnable());

        languageRepository.save(language);
        return languageMapper.mapEntityToDto(language);
    }

    private Language getLanguage(String langCode) {
        return languageRepository.findById(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException(LANGUAGE_NOT_FOUND);
        });
    }
}
