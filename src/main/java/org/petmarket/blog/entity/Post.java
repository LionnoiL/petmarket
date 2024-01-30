package org.petmarket.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.petmarket.users.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "blog_posts")
@EntityListeners(AuditingEntityListener.class)
@Indexed
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User user;
    @CreatedDate
    @Column(name = "created")
    private LocalDateTime created;
    @LastModifiedDate
    @Column(name = "updated")
    @GenericField(sortable = Sortable.YES)
    private LocalDateTime updated;

    @Column(name = "reading_minutes")
    private int readingMinutes;

    @NotNull
    @Column(name = "alias")
    private String alias;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToMany
    @JoinTable(name = "post_categories",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @IndexedEmbedded
    private List<BlogCategory> categories = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", cascade = CascadeType.ALL)
    @IndexedEmbedded
    private List<PostTranslations> translations = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<BlogComment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "posts_attributes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id"))
    private List<BlogAttribute> attributes = new ArrayList<>();

    public enum Status {
        PUBLISHED, DRAFT
    }
}
