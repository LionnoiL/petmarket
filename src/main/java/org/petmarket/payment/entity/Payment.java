package org.petmarket.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.translate.TranslateHolder;

import java.util.Set;

@Entity
@Table(name = "pays")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements TranslateHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "enable")
    private Boolean enable;

    @Column(name = "alias", nullable = false)
    private String alias;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "payment", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<PaymentTranslate> translations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;

        return id.equals(payment.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
