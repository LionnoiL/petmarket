package org.petmarket.advertisements.attributes.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attribute")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sort_order")
    private int sortValue;

    @ManyToOne
    @JoinColumn(name = "attribute_group_id")
    private AttributeGroup group;
}
