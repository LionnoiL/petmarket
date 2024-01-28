package org.petmarket.blog.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.attribute.BlogPostAttributeRequestDto;
import org.petmarket.blog.dto.attribute.BlogPostAttributeResponseDto;
import org.petmarket.blog.entity.BlogAttribute;
import org.petmarket.blog.entity.BlogAttributeTranslation;
import org.petmarket.blog.mapper.BlogAttributeMapper;
import org.petmarket.blog.repository.BlogAttributeRepository;
import org.petmarket.blog.service.AttributeService;
import org.petmarket.errorhandling.ItemNotFoundException;
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
    @Override
    public BlogPostAttributeResponseDto save(BlogPostAttributeRequestDto blogPostAttributeRequestDto) {
        BlogAttribute attribute = BlogAttribute.builder()
                .sortValue(blogPostAttributeRequestDto.getSortValue())
                .build();
        BlogAttribute saved = attributeRepository.save(attribute);
        BlogAttributeTranslation translation = BlogAttributeTranslation.builder()
                .title(blogPostAttributeRequestDto.getTitle())
                .language(checkedLang(blogPostAttributeRequestDto.getLangCode()))
                .attribute(saved)
                .build();
        saved.setTranslations(List.of(translation));

        return mapper.toDto(saved, checkedLang(blogPostAttributeRequestDto.getLangCode()));
    }

    @Override
    public BlogPostAttributeResponseDto get(Long id, String langCode) {
        return mapper.toDto(attributeRepository.findById(id).orElseThrow(), checkedLang(langCode));
    }

    @Override
    public List<BlogPostAttributeResponseDto> getAll(Pageable pageable, String langCode) {
        return attributeRepository.findAll(pageable).stream()
                .map(attribute -> mapper.toDto(attribute, checkedLang(langCode)))
                .toList();
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
            if (translation.getLanguage().getLangCode().equals(blogPostAttributeRequestDto.getLangCode())) {
                translation.setTitle(blogPostAttributeRequestDto.getTitle());
            }
        }

        return mapper.toDto(attributeRepository.save(attribute), checkedLang(langCode));
    }

    private BlogAttribute findById(Long id) {
        return attributeRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Can't find Blog Attribute with id: " + id));
    }

    private Language checkedLang(String langCode) {
        return languageService.getByLangCode(langCode);
    }
}
