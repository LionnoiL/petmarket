package org.petmarket.advertisements.attributes.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.translate.TranslateHolder;

import java.util.Set;

@Entity
@Table(name = "attribute")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Set<AttributeTranslate> translations;
}
