package org.petmarket.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.petmarket.language.entity.Language;
import org.petmarket.translate.LanguageHolder;

@Entity
@Table(name = "blog_categories_translations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Indexed
public class CategoryTranslation implements LanguageHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;
    @NotNull
    @Column(name = "category_name")
    @FullTextField
    private String title;
    @NotNull
    @Column(name = "category_description")
    @FullTextField
    private String description;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private BlogCategory blogCategory;
}
