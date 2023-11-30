package org.petmarket.payment.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.payment.dto.PaymentResponseDto;
import org.petmarket.payment.entity.Payment;
import org.petmarket.payment.entity.PaymentTranslate;
import org.petmarket.translate.LanguageHolder;
import org.petmarket.translate.TranslationService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentResponseTranslateMapper {

    private final OptionsService optionsService;
    private final TranslationService translationService;
    private final PaymentMapper paymentMapper;

    public PaymentResponseDto mapEntityToDto(Payment entity, Language language) {
        if (entity == null) {
            return null;
        }
        PaymentTranslate translation = (PaymentTranslate) translationService.getTranslate(
                entity.getTranslations().stream().map(LanguageHolder.class::cast).collect(Collectors.toSet()),
                language,
                optionsService.getDefaultSiteLanguage()
        );
        PaymentResponseDto paymentResponseDto = paymentMapper.mapEntityToDto(entity);
        paymentResponseDto.setName(translation.getName());
        paymentResponseDto.setDescription(translation.getDescription());
        paymentResponseDto.setLangCode(language.getLangCode());

        return paymentResponseDto;
    }

    public List<PaymentResponseDto> mapEntityToDto(List<Payment> payments,
                                                   Language language) {
        if (payments == null) {
            return Collections.emptyList();
        }
        return payments.stream()
                .map(p -> mapEntityToDto(p, language))
                .toList();
    }
}
