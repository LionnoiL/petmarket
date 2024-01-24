package org.petmarket.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "blog_categories")
@Indexed
public class BlogCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "blogCategory", cascade = CascadeType.ALL)
    @IndexedEmbedded
    private List<CategoryTranslation> translations = new ArrayList<>();
    @ManyToMany(mappedBy = "categories")
    private List<Post> posts = new ArrayList<>();

    @Override
    public String toString() {
        return "BlogCategory{" +
                "id=" + id +
                ", translations=" + translations +
                '}';
    }
}
