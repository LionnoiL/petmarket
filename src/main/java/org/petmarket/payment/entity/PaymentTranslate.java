package org.petmarket.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.petmarket.language.entity.Language;
import org.petmarket.translate.LanguageHolder;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pays_translation")
public class PaymentTranslate implements LanguageHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lang_code")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Payment payment;

    @Column(name = "title")
    private String name;

    @Column(name = "description")
    private String description;
}
