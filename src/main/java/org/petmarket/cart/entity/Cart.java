package org.petmarket.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.users.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public int getTotalQuantity() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public BigDecimal getTotalSum() {
        return items.stream()
                .map(CartItem::getCartItemSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getItemByAdvertisementId(Long advertisementId) {
        if (advertisementId == null) {
            return null;
        }
        return this.getItems().stream()
                .filter(item -> item.getAdvertisement().getId().equals(advertisementId))
                .findAny().orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Cart cart = (Cart) o;

        return id.equals(cart.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
