package org.petmarket.blog.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.petmarket.blog.dto.attribute.BlogPostAttributeRequestDto;
import org.petmarket.blog.dto.attribute.BlogPostAttributeResponseDto;
import org.petmarket.blog.dto.attribute.BlogPostAttributeTranslateDto;
import org.petmarket.blog.entity.BlogAttribute;
import org.petmarket.blog.entity.BlogAttributeTranslation;
import org.petmarket.blog.mapper.BlogAttributeMapper;
import org.petmarket.blog.repository.BlogAttributeRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.errorhandling.ItemNotUpdatedException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttributeServiceImplTest {

    @InjectMocks
    private AttributeServiceImpl attributeService;

    @Mock
    private BlogAttributeRepository attributeRepository;

    @Mock
    private BlogAttributeMapper mapper;

    @Mock
    private LanguageService languageService;

    @Test
    public void testSaveAttribute() {
        // Arrange
        String langCode = "en";
        BlogPostAttributeRequestDto requestDto = new BlogPostAttributeRequestDto();
        requestDto.setSortValue(1);
        requestDto.setTitle("Test Title");

        BlogAttribute attribute = new BlogAttribute();
        attribute.setSortValue(requestDto.getSortValue());

        BlogAttribute savedAttribute = new BlogAttribute();
        savedAttribute.setSortValue(requestDto.getSortValue());

        BlogPostAttributeResponseDto responseDto = new BlogPostAttributeResponseDto();
        responseDto.setSortValue(requestDto.getSortValue());
        responseDto.setTitle(requestDto.getTitle());
        responseDto.setLangCode(langCode);

        Language language = new Language();
        language.setLangCode(langCode);

        when(attributeRepository.save(any(BlogAttribute.class))).thenReturn(savedAttribute);
        when(languageService.getByLangCode(anyString())).thenReturn(language);
        when(mapper.toDto(any(BlogAttribute.class), any(Language.class))).thenReturn(responseDto);

        // Act
        BlogPostAttributeResponseDto result = attributeService.saveAttribute(requestDto, langCode);

        // Assert
        assertEquals(responseDto, result);
        verify(attributeRepository).save(attribute);
        verify(languageService, atLeast(1)).getByLangCode(langCode);
        verify(mapper).toDto(savedAttribute, language);
    }

    @Test
    public void testUpdateById() {
        // Arrange
        Long id = 1L;
        String langCode = "en";
        BlogPostAttributeRequestDto requestDto = new BlogPostAttributeRequestDto();
        requestDto.setSortValue(1);
        requestDto.setTitle("Test Title");

        BlogAttribute attribute = new BlogAttribute();
        attribute.setId(id);
        attribute.setSortValue(requestDto.getSortValue());

        BlogAttribute updatedAttribute = new BlogAttribute();
        updatedAttribute.setId(id);
        updatedAttribute.setSortValue(requestDto.getSortValue());

        BlogPostAttributeResponseDto responseDto = new BlogPostAttributeResponseDto();
        responseDto.setSortValue(requestDto.getSortValue());
        responseDto.setTitle(requestDto.getTitle());
        responseDto.setLangCode(langCode);

        Language language = new Language();
        language.setLangCode(langCode);

        when(attributeRepository.findById(anyLong())).thenReturn(Optional.of(attribute));
        when(attributeRepository.save(any(BlogAttribute.class))).thenReturn(updatedAttribute);
        when(languageService.getByLangCode(anyString())).thenReturn(language);
        when(mapper.toDto(any(BlogAttribute.class), any(Language.class))).thenReturn(responseDto);

        // Act
        BlogPostAttributeResponseDto result = attributeService.updateById(id, langCode, requestDto);

        // Assert
        assertEquals(responseDto, result);
        verify(attributeRepository).findById(id);
        verify(attributeRepository).save(attribute);
        verify(languageService).getByLangCode(langCode);
        verify(mapper).toDto(updatedAttribute, language);
    }

    @Test
    void addTranslationShouldAddTranslationWhenNotExists() {
        // Arrange
        Long attributeId = 1L;
        String langCode = "en";
        BlogPostAttributeTranslateDto translationDto = new BlogPostAttributeTranslateDto();
        translationDto.setTitle("Test Title");

        BlogAttribute attribute = new BlogAttribute();
        attribute.setId(attributeId);
        attribute.setTranslations(new ArrayList<>());

        when(attributeRepository.findById(anyLong())).thenReturn(Optional.of(attribute));
        when(languageService.getByLangCode(anyString())).thenReturn(new Language());

        // Act and Assert
        attributeService.addTranslation(attributeId, langCode, translationDto);

        verify(attributeRepository).findById(attributeId);
        verify(languageService).getByLangCode(langCode);
    }

    @Test
    void addTranslationShouldThrowExceptionWhenTranslationExists() {
        // Arrange
        Long attributeId = 1L;
        String langCode = "en";
        BlogPostAttributeTranslateDto translationDto = new BlogPostAttributeTranslateDto();
        translationDto.setTitle("Test Title");

        Language language = new Language();
        language.setLangCode(langCode);

        BlogAttributeTranslation translation = new BlogAttributeTranslation();
        translation.setLanguage(language);

        BlogAttribute attribute = new BlogAttribute();
        attribute.setId(attributeId);
        attribute.setTranslations(List.of(translation));

        when(attributeRepository.findById(anyLong())).thenReturn(Optional.of(attribute));

        // Act and Assert
        assertThrows(ItemNotUpdatedException.class,
                () -> attributeService.addTranslation(attributeId, langCode, translationDto));

        verify(attributeRepository).findById(attributeId);
    }

    @Test
    void saveShouldReturnNull() {
        // Arrange
        BlogPostAttributeRequestDto requestDto = new BlogPostAttributeRequestDto();
        // Act and Assert
        assertNull(attributeService.save(requestDto));
    }

    @Test
    void getShouldReturnDtoWhenAttributeExists() {
        // Arrange
        Long id = 1L;
        String langCode = "en";

        when(attributeRepository.findById(anyLong())).thenReturn(Optional.of(new BlogAttribute()));
        when(languageService.getByLangCode(anyString())).thenReturn(new Language());
        when(mapper.toDto(any(BlogAttribute.class),
                any(Language.class))).thenReturn(new BlogPostAttributeResponseDto());

        // Act
        attributeService.get(id, langCode);

        // Assert
        verify(attributeRepository).findById(id);
        verify(languageService).getByLangCode(langCode);
    }

    @Test
    void getShouldThrowExceptionWhenAttributeDoesNotExist() {
        // Arrange
        Long id = 1L;
        String langCode = "en";

        when(attributeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ItemNotFoundException.class, () -> attributeService.get(id, langCode));

        verify(attributeRepository).findById(id);
    }

    @Test
    void getAllShouldReturnDtoList() {
        // Arrange
        String langCode = "en";
        Pageable pageable = Pageable.unpaged();


        when(attributeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new BlogAttribute())));
        when(languageService.getByLangCode(anyString())).thenReturn(new Language());
        when(mapper.toDto(any(BlogAttribute.class), any(Language.class)))
                .thenReturn(new BlogPostAttributeResponseDto());

        // Act
        attributeService.getAll(pageable, langCode);

        // Assert
        verify(attributeRepository).findAll(pageable);
        verify(languageService, atLeast(1)).getByLangCode(langCode);
    }

    @Test
    void getBlogAttributesShouldReturnAttributeList() {
        // Arrange
        List<Long> attributeIds = List.of(1L, 2L);

        when(attributeRepository.findAllById(anyList())).thenReturn(List.of(new BlogAttribute()));

        // Act
        attributeService.getBlogAttributes(attributeIds);

        // Assert
        verify(attributeRepository).findAllById(attributeIds);
    }

    @Test
    void findByIdShouldReturnAttributeWhenExists() {
        // Arrange
        Long id = 1L;

        when(attributeRepository.findById(anyLong())).thenReturn(Optional.of(new BlogAttribute()));

        // Act
        attributeService.findById(id);

        // Assert
        verify(attributeRepository).findById(id);
    }

    @Test
    void findByIdShouldThrowExceptionWhenDoesNotExist() {
        // Arrange
        Long id = 1L;

        when(attributeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ItemNotFoundException.class, () -> attributeService.findById(id));

        verify(attributeRepository).findById(id);
    }

    @Test
    void deleteShouldDeleteWhenExists() {
        // Arrange
        Long id = 1L;

        // Act
        attributeService.delete(id);

        // Assert
        verify(attributeRepository).deleteById(id);
    }
}
