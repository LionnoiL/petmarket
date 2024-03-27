package org.petmarket.location.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "states")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alias", nullable = false)
    private String alias;

    @Column(name = "koatuu_code", nullable = false)
    private String koatuuCode;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
    private Set<City> cities = new HashSet<>();
}
