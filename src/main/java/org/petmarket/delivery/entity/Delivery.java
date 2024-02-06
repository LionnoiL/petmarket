package org.petmarket.delivery.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.translate.TranslateHolder;

import java.util.Set;

@Entity
@Table(name = "deliveries")
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery implements TranslateHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "enable")
    private Boolean enable;

    @Column(name = "alias", nullable = false)
    private String alias;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "delivery", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<DeliveryTranslate> translations;
}
