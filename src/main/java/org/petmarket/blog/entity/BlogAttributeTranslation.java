package org.petmarket.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.language.entity.Language;
import org.petmarket.translate.LanguageHolder;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blog_attribute_translation")
public class BlogAttributeTranslation implements LanguageHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private BlogAttribute attribute;

    @Column(name = "title")
    private String title;
}
