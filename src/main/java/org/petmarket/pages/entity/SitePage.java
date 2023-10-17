package org.petmarket.pages.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "pages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SitePage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreatedDate
    @Column(name = "created")
    private LocalDate created;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDate updated;

    @Column(name = "page_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SitePageType type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sitePage", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Translate> translations;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SitePage sitePage = (SitePage) o;

        return id.equals(sitePage.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
