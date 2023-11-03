package org.petmarket.advertisements.attributes.entity;

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
@Table(name = "attribute_groups_translation")
public class AttributeGroupTranslate implements LanguageHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AttributeGroup group;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
}
