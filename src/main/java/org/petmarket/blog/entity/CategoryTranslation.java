package org.petmarket.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "blog_categories_translations")
@Data
public class CategoryTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "lang_code")
    private String langCode;
    @NotNull
    @Column(name = "category_name")
    private String categoryName;
    @NotNull
    @Column(name = "category_description")
    private String categoryDescription;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private BlogCategory blogCategory;
}
