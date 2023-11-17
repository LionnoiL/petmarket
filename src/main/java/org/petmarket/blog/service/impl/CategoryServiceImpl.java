package org.petmarket.blog.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.category.BlogPostCategoryRequestDto;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.entity.BlogCategory;
import org.petmarket.blog.entity.CategoryTranslation;
import org.petmarket.blog.mapper.CategoryMapper;
import org.petmarket.blog.repository.CategoryRepository;
import org.petmarket.blog.service.CategoryService;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.petmarket.options.service.OptionsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final OptionsService optionsService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;
    private final LanguageService languageService;

    @Transactional
    @Override
    public BlogPostCategoryResponseDto save(BlogPostCategoryRequestDto requestDto) {
        BlogCategory blogCategory = new BlogCategory();
        List<CategoryTranslation> translations = new ArrayList<>();
        translations.add(createTranslation(requestDto,
                optionsService.getDefaultSiteLanguage().getLangCode(),
                blogCategory));
        blogCategory.setTranslations(translations);
        categoryRepository.save(blogCategory);
        return mapper.toDto(blogCategory, optionsService.getDefaultSiteLanguage());
    }

    @Override
    public BlogPostCategoryResponseDto get(Long categoryId, String langCode) {
        BlogCategory category = getBlogCategory(categoryId);
        return mapper.toDto(category, checkedLang(langCode));
    }

    @Override
    public List<BlogPostCategoryResponseDto> getAll(Pageable pageable, String langCode) {
        return categoryRepository.findAll(pageable).stream()
                .map(c -> mapper.toDto(c, checkedLang(langCode)))
                .toList();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    @Override
    public BlogPostCategoryResponseDto updateById(Long categoryId,
                                                  String langCode,
                                                  BlogPostCategoryRequestDto requestDto) {

        BlogCategory category = getBlogCategory(categoryId);

        boolean isLangCodeExist = category.getTranslations().stream()
                .anyMatch(t -> t.getLanguage().getLangCode().equals(checkedLang(langCode).getLangCode()));

        if (isLangCodeExist) {
            List<CategoryTranslation> translations = category.getTranslations().stream()
                    .filter(t -> t.getLanguage().getLangCode().equals(checkedLang(langCode).getLangCode()))
                    .toList();

            for (CategoryTranslation translation : translations) {
                translation.setTitle(requestDto.getTitle());
                translation.setDescription(requestDto.getDescription());
            }

            categoryRepository.save(category);
        } else {
            throw new ItemNotUpdatedException("No Translation for this Language: "
                    + langCode
                    + ". Create translation first");
        }

        return mapper.toDto(category, checkedLang(langCode));
    }

    @Override
    public BlogCategory getBlogCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new ItemNotFoundException("Can't find category with id: " + categoryId)
        );
    }

    @Transactional
    @Override
    public BlogPostCategoryResponseDto addTranslation(Long categoryId,
                                                      String langCode,
                                                      BlogPostCategoryRequestDto requestDto) {
        BlogCategory category = getBlogCategory(categoryId);
        List<CategoryTranslation> translations = category.getTranslations();
        if (translations.stream()
                .anyMatch(t -> t.getLanguage().getLangCode()
                        .equals(checkedLang(langCode).getLangCode()))) {
            throw new ItemNotUpdatedException(langCode + " translation is already exist");
        } else {
            translations.add(createTranslation(requestDto, langCode, category));
            category.setTranslations(translations);
            categoryRepository.save(category);
        }
        return mapper.toDto(category, checkedLang(langCode));
    }

    private CategoryTranslation createTranslation(BlogPostCategoryRequestDto requestDto,
                                                  String langCode, BlogCategory category) {
        return CategoryTranslation.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .blogCategory(category)
                .language(languageService.getByLangCode(langCode)).build();
    }

    private Language checkedLang(String langCode) {
        return languageService.getByLangCode(langCode);
    }

}
