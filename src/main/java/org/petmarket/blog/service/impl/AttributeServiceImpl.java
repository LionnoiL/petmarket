package org.petmarket.blog.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.attribute.BlogPostAttributeRequestDto;
import org.petmarket.blog.dto.attribute.BlogPostAttributeResponseDto;
import org.petmarket.blog.dto.attribute.BlogPostAttributeTranslateDto;
import org.petmarket.blog.entity.BlogAttribute;
import org.petmarket.blog.entity.BlogAttributeTranslation;
import org.petmarket.blog.mapper.BlogAttributeMapper;
import org.petmarket.blog.repository.BlogAttributeRepository;
import org.petmarket.blog.service.AttributeService;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {
    private final BlogAttributeRepository attributeRepository;
    private final BlogAttributeMapper mapper;
    private final LanguageService languageService;

    @Transactional
    public BlogPostAttributeResponseDto saveAttribute(BlogPostAttributeRequestDto blogPostAttributeRequestDto,
                                                      String langCode) {
        BlogAttribute attribute = BlogAttribute.builder()
                .sortValue(blogPostAttributeRequestDto.getSortValue())
                .build();
        BlogAttribute saved = attributeRepository.save(attribute);
        BlogAttributeTranslation translation = BlogAttributeTranslation.builder()
                .title(blogPostAttributeRequestDto.getTitle())
                .language(checkedLang(langCode))
                .attribute(saved)
                .build();
        saved.setTranslations(List.of(translation));

        return mapper.toDto(saved, checkedLang(langCode));
    }

    @Transactional
    @Override
    public BlogAttributeTranslation addTranslation(Long attributeId, String langCode,
                                                   BlogPostAttributeTranslateDto translationDto) {
        BlogAttribute attribute = findById(attributeId);

        for (BlogAttributeTranslation translation : attribute.getTranslations()) {
            if (translation.getLanguage().getLangCode().equals(langCode)) {
                throw new ItemNotUpdatedException(langCode + " translation already exist");
            }
        }

        BlogAttributeTranslation translation = BlogAttributeTranslation.builder()
                .title(translationDto.getTitle())
                .language(checkedLang(langCode))
                .attribute(attribute)
                .build();
        attribute.getTranslations().add(translation);
        return translation;
    }

    @Transactional
    @Override
    public BlogPostAttributeResponseDto save(BlogPostAttributeRequestDto blogPostAttributeRequestDto) {
        return null;
    }

    @Override
    public BlogPostAttributeResponseDto get(Long id, String langCode) {
        return mapper.toDto(attributeRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Can't find Blog Attribute with id: " + id)),
                checkedLang(langCode));
    }

    @Override
    public List<BlogPostAttributeResponseDto> getAll(Pageable pageable, String langCode) {
        return attributeRepository.findAll(pageable).stream()
                .map(attribute -> mapper.toDto(attribute, checkedLang(langCode)))
                .toList();
    }

    @Override
    public List<BlogAttribute> getBlogAttributes(List<Long> attributeIds) {
        return attributeRepository.findAllById(attributeIds);
    }

    @Override
    public BlogAttribute findById(Long id) {
        return attributeRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Can't find Blog Attribute with id: " + id));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        attributeRepository.deleteById(id);
    }

    @Transactional
    @Override
    public BlogPostAttributeResponseDto updateById(Long id,
                                                   String langCode,
                                                   BlogPostAttributeRequestDto blogPostAttributeRequestDto) {
        BlogAttribute attribute = findById(id);
        attribute.setSortValue(blogPostAttributeRequestDto.getSortValue());
        for (BlogAttributeTranslation translation : attribute.getTranslations()) {
            if (translation.getLanguage().getLangCode().equals(langCode)) {
                translation.setTitle(blogPostAttributeRequestDto.getTitle());
            }
        }

        return mapper.toDto(attributeRepository.save(attribute), checkedLang(langCode));
    }

    private Language checkedLang(String langCode) {
        return languageService.getByLangCode(langCode);
    }
}
