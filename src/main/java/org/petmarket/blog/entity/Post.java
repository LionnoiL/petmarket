package org.petmarket.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.petmarket.users.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "blog_posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User user;
    @CreatedDate
    @Column(name = "created")
    private Date created;
    @LastModifiedDate
    @Column(name = "updated")
    private Date updated;
    @ManyToMany
    @JoinTable(name = "post_categories",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostTranslations> translations = new ArrayList<>();
}
