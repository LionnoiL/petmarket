package org.petmarket.location.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cities_types")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "type", cascade = CascadeType.ALL)
    private List<CityTypeTranslate> translations = new ArrayList<>();
}
