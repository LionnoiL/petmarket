package org.petmarket.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.delivery.entity.Delivery;
import org.petmarket.payment.entity.Payment;
import org.petmarket.users.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreatedDate
    @Column(name = "created")
    private LocalDate created;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "delvery_id")
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "pay_id")
    private Payment payment;

    @Column(name = "order_comment")
    private String comment;
}
