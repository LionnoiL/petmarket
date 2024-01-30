package org.petmarket.advertisements.category.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.translate.TranslateHolder;

import java.util.Objects;
import java.util.Set;

@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed
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
    @IndexedEmbedded
    private Set<AdvertisementCategoryTranslate> translations;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Advertisement> advertisements;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdvertisementCategory category = (AdvertisementCategory) o;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
