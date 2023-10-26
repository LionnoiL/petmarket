package org.petmarket.breeds.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Entity
@Data
@Table(name = "animal_breeds_translation")
public class BreedTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "lang_code")
    private String langCode;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Breed breed;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
}
