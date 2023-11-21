package org.petmarket.location.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alias", nullable = false)
    private String alias;

    @Column(name = "koatuu_code", nullable = false)
    private String koatuuCode;

    @Column(name = "city_type_name", nullable = false)
    private String cityTypeName;

    @Column(name = "city_type_short_name", nullable = false)
    private String cityTypeShortName;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;
}
