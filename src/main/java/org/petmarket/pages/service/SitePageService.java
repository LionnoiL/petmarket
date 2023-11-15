package org.petmarket.pages.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.options.service.OptionsService;
import org.petmarket.pages.dto.SitePageCreateRequestDto;
import org.petmarket.pages.dto.SitePageResponseDto;
import org.petmarket.pages.dto.SitePageUpdateRequestDto;
import org.petmarket.pages.entity.SitePage;
import org.petmarket.pages.entity.SitePageTranslate;
import org.petmarket.pages.entity.SitePageType;
import org.petmarket.pages.mapper.SitePageMapper;
import org.petmarket.pages.mapper.SitePageResponseTranslateMapper;
import org.petmarket.pages.repository.SitePageRepository;
import org.petmarket.translate.TranslateException;
import org.petmarket.utils.ErrorUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.petmarket.utils.MessageUtils.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class SitePageService {

    private final SitePageRepository pageRepository;
    private final LanguageRepository languageRepository;
    private final SitePageMapper pageMapper;
    private final SitePageResponseTranslateMapper sitePageResponseTranslateMapper;
    private final OptionsService optionsService;

    @Transactional
    public SitePageResponseDto addPage(SitePageCreateRequestDto request, BindingResult bindingResult) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();
        SitePage page = pageMapper.mapDtoRequestToEntity(request);
        page.setTranslations(new HashSet<>());
        SitePageTranslate translation = SitePageTranslate.builder()
                .id(null)
                .sitePage(page)
                .description(request.getDescription())
                .title(request.getTitle())
                .language(defaultSiteLanguage)
                .build();
        addTranslation(page, translation);

        pageRepository.save(page);

        log.info("The Page was created");
        return sitePageResponseTranslateMapper.mapEntityToDto(page, defaultSiteLanguage);
    }

    @Transactional
    public SitePageResponseDto updatePage(Long id, String langCode, SitePageUpdateRequestDto request,
                                          BindingResult bindingResult) {
        ErrorUtils.checkItemNotUpdatedException(bindingResult);

        Language language = getLanguage(langCode);
        SitePage page = getSitePage(id);

        page.setType(request.getType());
        page.setUpdated(LocalDate.now());

        SitePageTranslate translation;
        if (checkLanguage(page, language)) {
            translation = getTranslation(page, language);
        } else {
            translation = new SitePageTranslate();
            addTranslation(page, translation);
            translation.setSitePage(page);
            translation.setLanguage(language);
        }
        translation.setTitle(request.getTitle());
        translation.setDescription(request.getDescription());

        pageRepository.save(page);

        log.info("The Page was updated");
        return sitePageResponseTranslateMapper.mapEntityToDto(page, language);
    }

    public Collection<SitePageResponseDto> getAll(String langCode) {
        Language language = getLanguage(langCode);
        List<SitePage> sitePages = pageRepository.findAll();

        return sitePageResponseTranslateMapper.mapEntityToDto(sitePages, language);
    }

    public SitePageResponseDto findById(Long id, String langCode) {
        Language language = getLanguage(langCode);
        return sitePageResponseTranslateMapper.mapEntityToDto(getSitePage(id), language);
    }

    public void deletePage(Long id) {
        SitePage page = getSitePage(id);
        pageRepository.deleteById(page.getId());
    }

    public SitePageResponseDto findByType(SitePageType pageType, String langCode) {
        Language language = getLanguage(langCode);
        SitePage sitePage = pageRepository.findByType(pageType);

        return sitePageResponseTranslateMapper.mapEntityToDto(sitePage, language);
    }

    private SitePageTranslate getTranslation(SitePage page, Language language) {
        return page.getTranslations().stream()
                .filter(t -> t.getLanguage().equals(language))
                .findFirst().orElseThrow(() -> new TranslateException(NO_TRANSLATION));
    }

    private boolean checkLanguage(SitePage page, Language language) {
        return page.getTranslations()
                .stream()
                .anyMatch(t -> t.getLanguage().equals(language));
    }

    private void addTranslation(SitePage page, SitePageTranslate translation) {
        if (checkLanguage(page, translation.getLanguage())) {
            throw new TranslateException(LANGUAGE_IS_PRESENT_IN_LIST);
        }
        translation.setSitePage(page);
        page.getTranslations().add(translation);
    }

    private Language getLanguage(String langCode) {
        return languageRepository.findByLangCodeAndEnableIsTrue(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException(LANGUAGE_NOT_FOUND);
        });
    }

    private SitePage getSitePage(Long id) {
        return pageRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(PAGE_NOT_FOUND);
        });
    }
}
