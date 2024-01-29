package org.petmarket.blog.service;

import org.petmarket.blog.dto.attribute.BlogPostAttributeRequestDto;
import org.petmarket.blog.dto.attribute.BlogPostAttributeResponseDto;
import org.petmarket.blog.dto.attribute.BlogPostAttributeTranslateDto;
import org.petmarket.blog.entity.BlogAttribute;
import org.petmarket.blog.entity.BlogAttributeTranslation;

import java.util.List;

public interface AttributeService extends AbstractService<BlogPostAttributeResponseDto,
        BlogPostAttributeRequestDto> {
    List<BlogAttribute> getBlogAttributes(List<Long> attributeIds);

    BlogAttribute findById(Long id);

    BlogPostAttributeResponseDto saveAttribute(BlogPostAttributeRequestDto blogPostAttributeRequestDto,
                                               String langCode);

    BlogAttributeTranslation addTranslation(Long attributeId, String langCode,
                                            BlogPostAttributeTranslateDto translation);
}
