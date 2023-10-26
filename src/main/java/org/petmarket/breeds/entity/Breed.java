package org.petmarket.breeds.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "animal_breeds")
@EntityListeners(AuditingEntityListener.class)
public class Breed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @Column(name = "created")
    private LocalDateTime created;
    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updated;
    @Column(name = "category_id")
    private Long categoryId;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "breed", cascade = CascadeType.ALL)
    private List<BreedTranslation> translations;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "breed", cascade = CascadeType.REMOVE)
    private List<BreedComment> comments;
}
