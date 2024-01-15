package org.petmarket.location.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@Table(name = "cities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @DocumentId
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

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @OneToMany(mappedBy = "city")
    private List<Location> locations;
}
