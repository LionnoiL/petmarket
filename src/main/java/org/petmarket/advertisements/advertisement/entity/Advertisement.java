package org.petmarket.advertisements.advertisement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;
import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.images.entity.AdvertisementImage;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.delivery.entity.Delivery;
import org.petmarket.location.entity.Location;
import org.petmarket.payment.entity.Payment;
import org.petmarket.translate.TranslateHolder;
import org.petmarket.users.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "advertisements")
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Indexed
public class Advertisement implements TranslateHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @GenericField
    private Long id;

    @CreatedDate
    @Column(name = "created")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated")
    @GenericField(sortable = Sortable.YES)
    private LocalDateTime updated;

    @Column(name = "ending")
    private LocalDate ending;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private AdvertisementCategory category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "alias", nullable = false)
    private String alias;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    @IndexedEmbedded
    private Location location;

    @Column(name = "advertisement_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @GenericField
    private AdvertisementStatus status;

    @Column(name = "price")
    @ScaledNumberField(sortable = Sortable.YES)
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "advertisement_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdvertisementType type;

    @Column(name = "rating")
    @GenericField(sortable = Sortable.YES)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "breed_id")
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private Breed breed;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "advertisement", fetch = FetchType.LAZY, orphanRemoval = true)
    @IndexedEmbedded
    private Set<AdvertisementTranslate> translations;

    @ManyToMany
    @JoinTable(name = "advertisement_payments",
            joinColumns = @JoinColumn(name = "advertisement_id"),
            inverseJoinColumns = @JoinColumn(name = "pay_id"))
    private List<Payment> payments = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "advertisement_deliveries",
            joinColumns = @JoinColumn(name = "advertisement_id"),
            inverseJoinColumns = @JoinColumn(name = "delivery_id"))
    private List<Delivery> deliveries = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "attribute_values",
            joinColumns = @JoinColumn(name = "advertisement_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id"))
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "advertisement", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("mainImage DESC, id ASC")
    private Set<AdvertisementImage> images;
}
