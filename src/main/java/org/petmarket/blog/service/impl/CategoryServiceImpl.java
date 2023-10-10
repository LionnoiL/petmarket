package org.petmarket.blog.service.impl;

import org.petmarket.blog.dto.category.BlogPostCategoryRequestDto;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.entity.BlogCategory;
import org.petmarket.blog.entity.CategoryTranslation;
import org.petmarket.blog.mapper.CategoryMapper;
import org.petmarket.blog.repository.CategoryRepository;
import org.petmarket.blog.service.CategoryService;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String TEMPORARY_DEF_LANG_CODE = "ua";
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public BlogPostCategoryResponseDto save(BlogPostCategoryRequestDto requestDto) {
        BlogCategory blogCategory = new BlogCategory();
        List<CategoryTranslation> translations = new ArrayList<>();
        translations.add(createTranslation(requestDto, TEMPORARY_DEF_LANG_CODE, blogCategory));
        blogCategory.setTranslations(translations);
        categoryRepository.save(blogCategory);
        return mapper.categoryToDto(blogCategory);
    }

    @Override
    public BlogPostCategoryResponseDto get(Long id, String langCode) {
        List<CategoryTranslation> translations = getBlogCategory(id).getTranslations().stream()
                .filter(t -> t.getLangCode().equals(langCode))
                .collect(Collectors.toList());
        BlogCategory category = getBlogCategory(id);
        category.setTranslations(translations);
        return mapper.categoryToDto(category);
    }

    @Override
    public List<BlogPostCategoryResponseDto> getAll(Pageable pageable, String langCode) {
        return categoryRepository.findAll().stream()
                .map(mapper::categoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public BlogPostCategoryResponseDto updateById(
            Long id, String langCode, BlogPostCategoryRequestDto requestDto) {
        BlogCategory category = getBlogCategory(id);
        List<CategoryTranslation> translations = category.getTranslations();
        translations.add(createTranslation(requestDto, langCode, category));
        category.setTranslations(translations);
        categoryRepository.save(category);
        return mapper.categoryToDto(category);
    }

    private CategoryTranslation createTranslation(BlogPostCategoryRequestDto requestDto,
                                                  String langCode, BlogCategory category) {
        CategoryTranslation newTranslation = new CategoryTranslation();
        newTranslation.setCategoryName(requestDto.getTitle());
        newTranslation.setCategoryDescription(requestDto.getDescription());
        newTranslation.setBlogCategory(category);
        newTranslation.setLangCode(langCode);
        return newTranslation;
    }

    @Override
    public BlogCategory getBlogCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Can't find category with id: " + id)
        );
    }
}
