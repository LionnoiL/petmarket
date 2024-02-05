package org.petmarket.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.advertisements.advertisement.entity.Advertisement;

import java.math.BigDecimal;

@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "cart_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;

    @Column(name = "qty")
    private int quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "advertisement_title")
    private String title;

    public BigDecimal getCartItemSum() {
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}
