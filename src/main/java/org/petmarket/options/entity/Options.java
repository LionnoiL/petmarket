package org.petmarket.options.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.language.entity.Language;

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

    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;

    @Column(name = "phones")
    private String phones;
}
