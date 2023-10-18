package org.petmarket.advertisements.category.entity;

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
@Table(name = "categories_translation")
public class AdvertisementCategoryTranslate implements LanguageHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AdvertisementCategory category;

    @Column(name = "title")
    private String title;

    @Column(name = "tag_title")
    private String tagTitle;

    @Column(name = "description")
    private String description;
}
