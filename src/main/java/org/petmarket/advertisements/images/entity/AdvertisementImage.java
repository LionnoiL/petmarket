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

    @Column(name = "image_name", nullable = false)
    private String name;

    @Column(name = "main_image")
    private boolean mainImage;

    @Column(name = "image_url_big", nullable = false)
    private String url;

    @Column(name = "image_url_small", nullable = false)
    private String urlSmall;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;
}
