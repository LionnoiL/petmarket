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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<CategoryTranslation> translations = new ArrayList<>();

    /*@Enumerated(EnumType.STRING)
    private CategoryStatus status;

    public enum CategoryStatus {
        ENABLED, DISABLED
    }*/

    @Override
    public String toString() {
        return "Category{"
                + "id=" + id
                + '}';
    }
}
