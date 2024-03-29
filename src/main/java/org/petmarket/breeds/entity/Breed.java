package org.petmarket.breeds.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Indexed
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
    @JoinColumn(name = "category_id")
    @ManyToOne
    private AdvertisementCategory category;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "breed", cascade = CascadeType.ALL)
    @IndexedEmbedded
    private List<BreedTranslation> translations;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "breed", cascade = CascadeType.REMOVE)
    private List<BreedComment> comments;
    @OneToMany(mappedBy = "breed")
    private List<Advertisement> advertisements;
}
