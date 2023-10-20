package org.petmarket.advertisements.category.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.translate.TranslateHolder;

import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementCategory implements TranslateHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "alias", nullable = false)
    private String alias;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private AdvertisementCategory parent;

    @Column(name = "available_in_tags")
    private boolean availableInTags;

    @Column(name = "available_in_favorite")
    private boolean availableInFavorite;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<AdvertisementCategoryTranslate> translations;
}
