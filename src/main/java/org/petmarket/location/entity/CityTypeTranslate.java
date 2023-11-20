package org.petmarket.location.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.petmarket.language.entity.Language;
import org.petmarket.translate.TranslateHolder;

@Entity
@Table(name = "cities_types_translations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityTypeTranslate implements TranslateHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;

    @NotNull
    @Column(name = "name")
    private String title;

    @NotNull
    @Column(name = "short_name")
    private String shortTitle;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private CityType type;
}
