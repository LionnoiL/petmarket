package org.petmarket.pages.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotCreatedException;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.pages.dto.SitePageCreateRequestDto;
import org.petmarket.pages.dto.SitePageResponseDto;
import org.petmarket.pages.entity.SitePage;
import org.petmarket.pages.entity.SitePageTranslation;
import org.petmarket.pages.mapper.SitePageMapper;
import org.petmarket.pages.repository.SitePageRepository;
import org.petmarket.translate.TranslateException;
import org.petmarket.utils.ErrorUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SitePageService {

    private final SitePageRepository pageRepository;
    private final SitePageMapper pageMapper;
    private final ErrorUtils errorUtils;
    private final OptionsService optionsService;

    private boolean checkLanguage(SitePage page, Language language){
        return page.getTranslations()
            .stream()
            .anyMatch(t -> t.getLanguage().equals(language));
    }
    private void addTranslation(SitePage page, SitePageTranslation translation) {
        if (checkLanguage(page, translation.getLanguage())){
            throw new TranslateException("Language is present in list");
        }
        translation.setSitePage(page);
        page.getTranslations().add(translation);
    }

    private void removeTranslation(SitePage page, SitePageTranslation translation) {
        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();
        if (checkLanguage(page, defaultSiteLanguage)){
            throw new TranslateException("Language is default.");
        }
        translation.setSitePage(null);
        page.getTranslations().remove(translation);
    }

    @Transactional
    public SitePageResponseDto addPage(SitePageCreateRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemNotCreatedException(errorUtils.getErrorsString(bindingResult));
        }

        SitePage page = pageMapper.mapDtoRequestToEntity(request);
        page.setTranslations(new HashSet<>());
        SitePageTranslation translation = SitePageTranslation.builder()
                .id(null)
                .sitePage(page)
                .description(request.getDescription())
                .title(request.getTitle())
                .language(optionsService.getDefaultSiteLanguage())
                .build();
        addTranslation(page, translation);

        pageRepository.save(page);
        log.info("The Page was created");
        return pageMapper.mapEntityToDto(page);
    }

    public Collection<SitePageResponseDto> getAll() {
        List<SitePage> sitePages = pageRepository.findAll();
        return pageMapper.mapEntityToDto(sitePages);
    }
}
