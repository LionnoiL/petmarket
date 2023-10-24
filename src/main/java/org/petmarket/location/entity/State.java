package org.petmarket.location.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "states")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alias", nullable = false)
    private String alias;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
    private Set<City> cities = new HashSet<>();
}
