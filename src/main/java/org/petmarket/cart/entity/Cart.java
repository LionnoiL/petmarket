package org.petmarket.cart.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.users.entity.User;

@Entity
@EqualsAndHashCode(of = {"id"})
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
}
