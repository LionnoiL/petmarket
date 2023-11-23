package org.petmarket.location.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "districts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "koatuu_code", nullable = false)
    private String koatuuCode;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;
}
