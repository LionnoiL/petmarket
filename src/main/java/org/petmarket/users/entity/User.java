package org.petmarket.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.petmarket.language.entity.Language;
import org.petmarket.location.entity.Location;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "site")
    private String site;

    @Min(0)
    @Column(name = "rating")
    private int rating;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;
    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

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
