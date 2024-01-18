package org.petmarket.breeds.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.petmarket.language.entity.Language;
import org.petmarket.translate.LanguageHolder;

@Entity
@Data
@Table(name = "animal_breeds_translation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Indexed
public class BreedTranslation implements LanguageHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Breed breed;
    @FullTextField
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;

    @Override
    public String toString() {
        return "BreedTranslation{" +
                "id=" + id +
                ", langCode='" + language.getName() + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
