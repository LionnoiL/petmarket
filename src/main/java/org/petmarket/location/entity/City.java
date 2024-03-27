package org.petmarket.location.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import java.util.List;

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
    private Long id;

    @Column(name = "name", nullable = false)
    @FullTextField
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
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private State state;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @OneToMany(mappedBy = "city")
    private List<Location> locations;
}
