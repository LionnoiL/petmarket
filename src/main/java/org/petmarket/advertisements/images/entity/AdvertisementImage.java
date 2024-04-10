package org.petmarket.advertisements.images.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.advertisements.advertisement.entity.Advertisement;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "advertisements_images")
public class AdvertisementImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "main_image")
    private boolean mainImage;

    @Column(name = "image_url_big", nullable = false)
    private String url;

    @Column(name = "image_url_small", nullable = false)
    private String urlSmall;

    @Column(name = "advertisement_image_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdvertisementImageType type;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;
}
