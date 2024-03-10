package org.petmarket.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import java.util.HashSet;
import lombok.*;
import org.petmarket.cart.entity.Cart;
import org.petmarket.language.entity.Language;
import org.petmarket.location.entity.Location;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created")
    private Date created;

    @LastModifiedDate
    @Column(name = "updated")
    private Date updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "user_name")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "site")
    private String site;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "instagram_link")
    private String instagramLink;

    @Column(name = "twitter_link")
    private String twitterLink;

    @Min(0)
    @Column(name = "rating")
    private int rating;

    @Min(0)
    @Column(name = "reviews_count")
    private int reviewsCount;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_provider")
    private LoginProvider loginProvider;

    @Column(name = "email_confirmed")
    private boolean emailConfirmed;

    @Column(name = "email_confirm_code")
    private String emailConfirmCode;

    @Column(name = "user_avatar_url")
    private String userAvatarUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<UserPhone> phones = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", created=" + created +
                ", updated=" + updated +
                ", last activity=" + lastActivity +
                ", status=" + status +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
