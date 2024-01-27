package org.petmarket.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.translate.TranslateHolder;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(of = {"id"})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blog_attributes")
public class BlogAttribute implements TranslateHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sort_order")
    private int sortValue;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attribute", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BlogAttributeTranslate> translations = new ArrayList<>();

    @ManyToMany(mappedBy = "attributes")
    private List<Post> posts = new ArrayList<>();
}
