package org.petmarket.blog.service.impl;

import org.petmarket.blog.dto.category.BlogPostCategoryRequestDto;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.entity.Category;
import org.petmarket.blog.entity.CategoryTranslation;
import org.petmarket.blog.mapper.CategoryMapper;
import org.petmarket.blog.repository.CategoryRepository;
import org.petmarket.blog.service.CategoryService;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public BlogPostCategoryResponseDto save(BlogPostCategoryRequestDto blogPostCategoryRequestDto) {
        return null;
    }

    @Override
    public BlogPostCategoryResponseDto get(Long id) {
        return null;
    }

    @Override
    public List<BlogPostCategoryResponseDto> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public BlogPostCategoryResponseDto updateById(
            Long id, BlogPostCategoryRequestDto blogPostCategoryRequestDto) {
        return null;
    }

    @Override
    public BlogPostCategoryResponseDto saveWithLang(BlogPostCategoryRequestDto requestDto,
                                                    String langCode) {

        Category category = new Category();
        CategoryTranslation categoryTranslation = new CategoryTranslation();
        categoryTranslation.setCategoryName(requestDto.getTitle());
        categoryTranslation.setCategoryDescription(requestDto.getDescription());
        categoryTranslation.setLangCode(langCode);
        categoryTranslation.setCategory(category);
        List<CategoryTranslation> translations = new ArrayList<>(List.of(categoryTranslation));
        category.setTranslations(translations);

        categoryRepository.save(category);

        return mapper.categoryToDto(category, langCode);
    }

    @Override
    public BlogPostCategoryResponseDto getByIdAndLang(Long id, String langCode) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("Can't find category with id: " + id)
        );
        CategoryTranslation categoryTranslation = category.getTranslations().stream()
                .filter(t -> t.getLangCode().equals(langCode))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuchElementException("Cant' find elem")
                );
        return mapper.categoryToDto(category, langCode);
    }
}
