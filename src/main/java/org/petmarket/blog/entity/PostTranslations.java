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
@Table(name = "blog_posts_translations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Indexed
public class PostTranslations implements LanguageHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;
    @NotNull
    @Column(name = "title")
    @FullTextField
    private String title;
    @Column(name = "short_Text")
    @FullTextField
    private String shortText;
    @NotNull
    @Column(name = "text")
    @FullTextField
    private String description;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Post post;
}
