package org.petmarket.blog.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.petmarket.blog.dto.attribute.BlogPostAttributeRequestDto;
import org.petmarket.blog.dto.attribute.BlogPostAttributeResponseDto;
import org.petmarket.blog.entity.BlogAttribute;
import org.petmarket.blog.mapper.BlogAttributeMapper;
import org.petmarket.blog.repository.BlogAttributeRepository;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void testSave() {
        // Arrange
        BlogPostAttributeRequestDto requestDto = new BlogPostAttributeRequestDto();
        requestDto.setSortValue(1);
        requestDto.setTitle("Test Title");
        requestDto.setLangCode("en");

        BlogAttribute attribute = new BlogAttribute();
        attribute.setSortValue(requestDto.getSortValue());

        BlogAttribute savedAttribute = new BlogAttribute();
        savedAttribute.setSortValue(requestDto.getSortValue());

        BlogPostAttributeResponseDto responseDto = new BlogPostAttributeResponseDto();
        responseDto.setSortValue(requestDto.getSortValue());
        responseDto.setTitle(requestDto.getTitle());
        responseDto.setLangCode(requestDto.getLangCode());

        Language language = new Language();
        language.setLangCode(requestDto.getLangCode());

        when(attributeRepository.save(any(BlogAttribute.class))).thenReturn(savedAttribute);
        when(languageService.getByLangCode(anyString())).thenReturn(language);
        when(mapper.toDto(any(BlogAttribute.class), any(Language.class))).thenReturn(responseDto);

        // Act
        BlogPostAttributeResponseDto result = attributeService.save(requestDto);

        // Assert
        assertEquals(responseDto, result);
        verify(attributeRepository).save(attribute);
        verify(languageService, atLeast(1)).getByLangCode(requestDto.getLangCode());
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
        requestDto.setLangCode(langCode);

        BlogAttribute attribute = new BlogAttribute();
        attribute.setId(id);
        attribute.setSortValue(requestDto.getSortValue());

        BlogAttribute updatedAttribute = new BlogAttribute();
        updatedAttribute.setId(id);
        updatedAttribute.setSortValue(requestDto.getSortValue());

        BlogPostAttributeResponseDto responseDto = new BlogPostAttributeResponseDto();
        responseDto.setSortValue(requestDto.getSortValue());
        responseDto.setTitle(requestDto.getTitle());
        responseDto.setLangCode(requestDto.getLangCode());

        Language language = new Language();
        language.setLangCode(requestDto.getLangCode());

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
        verify(languageService).getByLangCode(requestDto.getLangCode());
        verify(mapper).toDto(updatedAttribute, language);
    }
}