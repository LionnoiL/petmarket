package org.petmarket.pages.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.language.entity.Language;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pages_translation")
public class Translate implements org.petmarket.translate.Translate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private SitePage sitePage;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
}
