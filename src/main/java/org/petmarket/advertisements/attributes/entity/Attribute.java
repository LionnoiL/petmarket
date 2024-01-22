package org.petmarket.advertisements.attributes.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.translate.TranslateHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "attribute")
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed
public class Attribute implements TranslateHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sort_order")
    private int sortValue;

    @ManyToOne
    @JoinColumn(name = "attribute_group_id")
    private AttributeGroup group;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute", fetch = FetchType.LAZY, orphanRemoval = true)
    @IndexedEmbedded
    private Set<AttributeTranslate> translations;

    @ManyToMany(mappedBy = "attributes")
    private List<Advertisement> advertisements = new ArrayList<>();
}
