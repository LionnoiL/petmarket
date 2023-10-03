package org.petmarket.language.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "languages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Language {

    @Id
    @Column(name = "lang_code", nullable = false)
    private String langCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "enable")
    private Boolean enable;
}
