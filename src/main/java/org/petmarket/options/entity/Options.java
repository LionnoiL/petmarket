package org.petmarket.options.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "options")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "options_key", nullable = false, unique = true)
    private OptionsKey key;

    @Column(name = "options_value")
    private String value;

    public Options(OptionsKey key) {
        this.key = key;
    }
}
