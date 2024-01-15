package org.petmarket.location.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.petmarket.advertisements.advertisement.entity.Advertisement;

@Entity
@Table(name = "locations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "city_id")
    @IndexedEmbedded
    private City city;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @OneToMany(mappedBy = "location")
    private List<Advertisement> advertisements;
}
