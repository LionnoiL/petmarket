package org.petmarket.advertisements.advertisement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
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
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Advertisement implements TranslateHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreatedDate
    @Column(name = "created")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updated;

    @Column(name = "ending")
    private LocalDate ending;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private AdvertisementCategory category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "alias", nullable = false)
    private String alias;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "advertisement_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdvertisementStatus status;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "advertisement_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdvertisementType type;

    @Column(name = "rating")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "breed_id")
    private Breed breed;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "advertisement", fetch = FetchType.LAZY, orphanRemoval = true)
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
    private List<Attribute> attributes = new ArrayList<>();
}
