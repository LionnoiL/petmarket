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

@Slf4j
@RequiredArgsConstructor
@Service
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final OptionsService optionsService;
    private final LanguageMapper languageMapper;
    private final ErrorUtils errorUtils;

    public Collection<LanguageResponseDto> getAll() {
        List<Language> languages = languageRepository.findAll();
        return languageMapper.mapEntityToDto(languages);
    }

    public Language getByLangCode(String langCode) {
        return languageRepository.findByLangCode(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException("Language " + "-" + langCode + "-" + " not found");
        });
    }

    @Transactional
    public LanguageResponseDto enableLanguage(String langCode) {
        Language language = languageRepository.findById(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException("Language not found");
        });
        language.setEnable(true);
        languageRepository.save(language);
        return languageMapper.mapEntityToDto(language);
    }

    @Transactional
    public LanguageResponseDto disableLanguage(String langCode) {
        Language language = languageRepository.findById(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException("Language not found");
        });

        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();
        if (language.equals(defaultSiteLanguage)) {
            throw new ItemNotUpdatedException("Language is default in site options");
        }

        language.setEnable(false);
        languageRepository.save(language);
        return languageMapper.mapEntityToDto(language);
    }

    @Transactional
    public LanguageResponseDto addLanguage(LanguageCreateRequestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotCreatedException(errorUtils.getErrorsString(bindingResult));
        }
        languageRepository.findById(dto.getLangCode())
                .ifPresent(language -> {
                    throw new ItemNotCreatedException("The language is already in the database");
                });

        Language language = languageMapper.mapDtoRequestToEntity(dto);
        languageRepository.save(language);
        log.info("The Language was created");
        return languageMapper.mapEntityToDto(language);
    }

    @Transactional
    public LanguageResponseDto updateLanguage(String langCode, LanguageUpdateRequestDto dto,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotUpdatedException(errorUtils.getErrorsString(bindingResult));
        }

        Language language = languageRepository.findById(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException("Language not found");
        });
        language.setName(dto.getName());
        language.setEnable(dto.getEnable());

        languageRepository.save(language);
        return languageMapper.mapEntityToDto(language);
    }
}
