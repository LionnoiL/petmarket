package org.petmarket.advertisements.attributes.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.translate.TranslateHolder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "attribute_groups")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AttributeGroup implements TranslateHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreatedDate
    @Column(name = "created")
    private LocalDate created;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDate updated;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private AdvertisementCategory category;

    @Column(name = "attribute_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AttributeType type;

    @Column(name = "sort_order")
    private int sortValue;

    @Column(name = "use_in_filter")
    private boolean useInFilter;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<AttributeGroupTranslate> translations;
}
