package org.petmarket.blog.controller;

import lombok.RequiredArgsConstructor;
import org.petmarket.blog.dto.category.BlogPostCategoryRequestDto;
import org.petmarket.blog.dto.category.BlogPostCategoryResponseDto;
import org.petmarket.blog.service.CategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/blog/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/{langCode}")
    public BlogPostCategoryResponseDto save(@RequestBody BlogPostCategoryRequestDto requestDto,
                                            @PathVariable String langCode) {
        return categoryService.saveWithLang(requestDto, langCode);
    }

    @GetMapping("/{id}/{langCode}")
    public BlogPostCategoryResponseDto get(@PathVariable Long id,
                                           @PathVariable String langCode) {
        BlogPostCategoryResponseDto byIdAndLang = categoryService.getByIdAndLang(id, langCode);
        System.out.println(byIdAndLang);
        return byIdAndLang;
    }
}
