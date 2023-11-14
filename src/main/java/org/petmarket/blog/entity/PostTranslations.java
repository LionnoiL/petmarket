package org.petmarket.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.petmarket.language.entity.Language;

@Entity
@Table(name = "blog_posts_translations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostTranslations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;
    @NotNull
    @Column(name = "title")
    private String title;
    @Column(name = "short_Text")
    private String shortText;
    @NotNull
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Post post;
}
