package org.petmarket.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "blog_categories")
public class BlogCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "blogCategory", cascade = CascadeType.ALL)
    private List<CategoryTranslation> translations = new ArrayList<>();

    @Override
    public String toString() {
        return "BlogCategory{" +
                "id=" + id +
                ", translations=" + translations +
                '}';
    }
}
