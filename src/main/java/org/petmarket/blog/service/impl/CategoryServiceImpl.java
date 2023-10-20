package org.petmarket.blog.service.impl;

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
import org.petmarket.language.service.LanguageService;
import org.petmarket.options.service.OptionsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final OptionsService optionsService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;
    private final LanguageService languageService;

    @Override
    public BlogPostCategoryResponseDto save(BlogPostCategoryRequestDto requestDto) {
        BlogCategory blogCategory = new BlogCategory();
        List<CategoryTranslation> translations = new ArrayList<>();
        translations.add(createTranslation(requestDto,
                optionsService.getDefaultSiteLanguage().getLangCode(),
                blogCategory));
        blogCategory.setTranslations(translations);
        categoryRepository.save(blogCategory);
        return mapper.categoryToDto(blogCategory);
    }

    @Override
    public BlogPostCategoryResponseDto get(Long categoryId, String langCode) {
        BlogCategory category = getBlogCategory(categoryId);
        category.setTranslations(getTranslation(categoryId, langCode));
        return mapper.categoryToDto(category);
    }

    @Override
    public List<BlogPostCategoryResponseDto> getAll(Pageable pageable, String langCode) {
        return categoryRepository.findAll().stream()
                .peek(category ->
                        category.setTranslations(getTranslation(category.getId(), langCode))
                )
                .map(mapper::categoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public BlogPostCategoryResponseDto updateById(Long categoryId,
                                                  String langCode,
                                                  BlogPostCategoryRequestDto requestDto) {
        BlogCategory category = getBlogCategory(categoryId);
        category.getTranslations().stream()
                .filter(translation -> translation.getLangCode().equals(checkedLang(langCode)))
                .peek(translation -> {
                    translation.setCategoryName(requestDto.getTitle());
                    translation.setCategoryDescription(requestDto.getDescription());
                })
                .toList();
        categoryRepository.save(category);
        return mapper.categoryToDto(category);
    }

    @Override
    public BlogCategory getBlogCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new ItemNotFoundException("Can't find category with id: " + categoryId)
        );
    }

    @Override
    public BlogPostCategoryResponseDto addTranslation(Long categoryId,
                                                      String langCode,
                                                      BlogPostCategoryRequestDto requestDto) {
        BlogCategory category = getBlogCategory(categoryId);
        List<CategoryTranslation> translations = category.getTranslations();
        if (translations.stream()
                .anyMatch(t -> t.getLangCode()
                        .equals(checkedLang(langCode)))) {
            throw new ItemNotUpdatedException(langCode + " translation is already exist");
        } else {
            translations.add(createTranslation(requestDto, langCode, category));
            category.setTranslations(translations);
            categoryRepository.save(category);
        }
        return mapper.categoryToDto(category);
    }

    private CategoryTranslation createTranslation(BlogPostCategoryRequestDto requestDto,
                                                  String langCode, BlogCategory category) {
        CategoryTranslation newTranslation = new CategoryTranslation();
        newTranslation.setCategoryName(requestDto.getTitle());
        newTranslation.setCategoryDescription(requestDto.getDescription());
        newTranslation.setBlogCategory(category);
        newTranslation.setLangCode(checkedLang(langCode));
        return newTranslation;
    }

    private String checkedLang(String langCode) {
        return languageService.getByLangCode(langCode).getLangCode();
    }

    private List<CategoryTranslation> getTranslation(Long categoryId, String langCode) {
        List<CategoryTranslation> translations = getBlogCategory(categoryId).getTranslations().stream()
                .filter(t -> t.getLangCode().equals(checkedLang(langCode)))
                .collect(Collectors.toList());

        if (translations.isEmpty()) {
            translations = getBlogCategory(categoryId).getTranslations().stream()
                    .filter(postTranslations -> postTranslations.getLangCode().equals(
                            optionsService.getDefaultSiteLanguage().getLangCode()))
                    .collect(Collectors.toList());
        }
        return translations;
    }
}
