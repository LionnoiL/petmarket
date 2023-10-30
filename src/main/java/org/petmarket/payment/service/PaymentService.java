package org.petmarket.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.language.repository.LanguageRepository;
import org.petmarket.options.service.OptionsService;
import org.petmarket.payment.dto.PaymentRequestDto;
import org.petmarket.payment.dto.PaymentResponseDto;
import org.petmarket.payment.entity.Payment;
import org.petmarket.payment.entity.PaymentTranslate;
import org.petmarket.payment.mapper.PaymentMapper;
import org.petmarket.payment.mapper.PaymentResponseTranslateMapper;
import org.petmarket.payment.repository.PaymentRepository;
import org.petmarket.translate.TranslateException;
import org.petmarket.utils.ErrorUtils;
import org.petmarket.utils.TransliterateUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    public static final String LANGUAGE_NOT_FOUND_MESSAGE = "Language not found";
    private static final String PAYMENT_NOT_FOUND_MESSAGE = "Payment not found";
    private final PaymentRepository paymentRepository;
    private final LanguageRepository languageRepository;
    private final PaymentResponseTranslateMapper paymentResponseTranslateMapper;
    private final PaymentMapper paymentMapper;
    private final OptionsService optionsService;
    private final TransliterateUtils transliterateUtils;

    public PaymentResponseDto findEnabledById(Long id, String langCode) {
        Language language = getLanguage(langCode);
        return paymentResponseTranslateMapper.mapEntityToDto(getEnablePayment(id), language);
    }

    public PaymentResponseDto findById(Long id, String langCode) {
        Language language = getLanguage(langCode);
        return paymentResponseTranslateMapper.mapEntityToDto(getPayment(id), language);
    }

    public Collection<PaymentResponseDto> getEnabled(String langCode) {
        Language language = getLanguage(langCode);
        List<Payment> payments = paymentRepository.findAllByEnableIsTrue();
        return paymentResponseTranslateMapper.mapEntityToDto(payments, language);
    }

    public Collection<PaymentResponseDto> getAll(String langCode) {
        Language language = getLanguage(langCode);
        List<Payment> payments = paymentRepository.findAll();
        return paymentResponseTranslateMapper.mapEntityToDto(payments, language);
    }

    public PaymentResponseDto addPayment(PaymentRequestDto request, BindingResult bindingResult) {
        ErrorUtils.checkItemNotCreatedException(bindingResult);

        Language defaultSiteLanguage = optionsService.getDefaultSiteLanguage();

        Payment payment = paymentMapper.mapDtoRequestToEntity(request);
        payment.setAlias(
                transliterateUtils.getAlias(
                        Payment.class.getSimpleName(),
                        request.getName()));

        payment.setTranslations(new HashSet<>());
        PaymentTranslate translation = PaymentTranslate.builder()
                .id(null)
                .payment(payment)
                .name(request.getName())
                .description(request.getDescription())
                .language(defaultSiteLanguage)
                .build();
        addTranslation(payment, translation);

        paymentRepository.save(payment);

        log.info("Payment created");
        return paymentResponseTranslateMapper.mapEntityToDto(payment, defaultSiteLanguage);
    }

    public PaymentResponseDto updatePayment(Long id, String langCode, PaymentRequestDto request,
                                            BindingResult bindingResult) {
        ErrorUtils.checkItemNotUpdatedException(bindingResult);

        Payment payment = getPayment(id);
        payment.setEnable(request.getEnable());

        Language language = getLanguage(langCode);
        PaymentTranslate translation;
        if (checkLanguage(payment, language)) {
            translation = getTranslation(payment, language);
        } else {
            translation = new PaymentTranslate();
            addTranslation(payment, translation);
            translation.setPayment(payment);
            translation.setLanguage(language);
        }
        translation.setName(request.getName());
        translation.setDescription(request.getDescription());

        paymentRepository.save(payment);

        log.info("The Payment was updated");
        return paymentResponseTranslateMapper.mapEntityToDto(payment, language);
    }

    public void deletePayment(Long id) {
        Payment payment = getPayment(id);
        paymentRepository.delete(payment);
    }

    private Language getLanguage(String langCode) {
        return languageRepository.findByLangCodeAndEnableIsTrue(langCode).orElseThrow(() -> {
            throw new ItemNotFoundException(LANGUAGE_NOT_FOUND_MESSAGE);
        });
    }

    private Payment getEnablePayment(Long id) {
        return paymentRepository.findByIdAndEnableIsTrue(id).orElseThrow(() -> {
            throw new ItemNotFoundException(PAYMENT_NOT_FOUND_MESSAGE);
        });
    }

    private Payment getPayment(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException(PAYMENT_NOT_FOUND_MESSAGE);
        });
    }

    private PaymentTranslate getTranslation(Payment payment, Language language) {
        return payment.getTranslations().stream()
                .filter(t -> t.getLanguage().equals(language))
                .findFirst().orElseThrow(() -> new TranslateException("No translation"));
    }

    private void addTranslation(Payment payment, PaymentTranslate translation) {
        if (checkLanguage(payment, translation.getLanguage())) {
            throw new TranslateException("Language is present in list");
        }
        translation.setPayment(payment);
        payment.getTranslations().add(translation);
    }

    private boolean checkLanguage(Payment payment, Language language) {
        return payment.getTranslations()
                .stream()
                .anyMatch(t -> t.getLanguage().equals(language));
    }
}
