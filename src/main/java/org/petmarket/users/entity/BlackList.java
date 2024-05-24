package org.petmarket.users.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users_black_list")
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
