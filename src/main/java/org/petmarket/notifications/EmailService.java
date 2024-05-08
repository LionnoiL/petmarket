package org.petmarket.notifications;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.language.entity.Language;
import org.petmarket.options.service.OptionsService;
import org.petmarket.translate.TranslationMessages;
import org.petmarket.translate.TranslationService;
import org.petmarket.users.entity.User;
import org.petmarket.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService implements NotificationService {

    private final JavaMailSender emailSender;
    private final TranslationService translationService;
    private final OptionsService optionsService;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${site.name}")
    private String siteName;

    @Override
    public void send(NotificationType type, Map<String, Object> fields, Language language, String email) {
        log.info("send message to {} with text: {}", email, fields.get("link"));

        fields.put("mailtoLink", "mailto:" + emailFrom);
        fields.put("email", emailFrom);
        fields.put("footer", "© " + Helper.getCurrentYear() + " " + siteName);

        String userLangCode;
        if (language == null) {
            userLangCode = optionsService.getDefaultSiteLanguage().getLangCode();
        } else {
            userLangCode = language.getLangCode();
        }
        String templateName = getTemplateName(type, userLangCode);

        switch (type) {
            case RESET_PASSWORD -> sendEmailMessage(
                    translationService.getTranslate(TranslationMessages.PASSWORD_CHANGE_REQUEST, userLangCode),
                    templateName,
                    fields,
                    email
            );
            case CHANGE_PASSWORD, UPDATE_PASSWORD -> sendEmailMessage(
                    translationService.getTranslate(TranslationMessages.PASSWORD_CHANGED_SUCCESSFULLY, userLangCode),
                    templateName,
                    fields,
                    email
            );
            case MAIL_UPDATE_SUCCESSFULLY -> sendEmailMessage(
                    translationService.getTranslate(TranslationMessages.MAIL_CHANGED_SUCCESSFULLY, userLangCode),
                    templateName,
                    fields,
                    email
            );
            default -> log.info("error sending mail to {}", email);
        }
    }

    @Override
    public void send(NotificationType type, Map<String, Object> fields, User user) {
        send(type, fields, getUserLanguageCode(user), user.getEmail());
    }

    private Language getUserLanguageCode(User user) {
        if (user.getLanguage() == null) {
            return optionsService.getDefaultSiteLanguage();
        } else {
            return user.getLanguage();
        }
    }

    private String getTemplateName(NotificationType type, String langCode) {
        StringBuilder result = new StringBuilder("email/");
        result.append(langCode);
        result.append("/");

        switch (type) {
            case RESET_PASSWORD -> result.append("reset-password.html");
            case CHANGE_PASSWORD -> result.append("reset-password-successfully.html");
            case UPDATE_PASSWORD -> result.append("update-password-successfully.html");
            case MAIL_UPDATE_SUCCESSFULLY -> result.append("update-email-successfully.html");
            default -> log.info("no template assigned for to {}", type);
        }
        return result.toString();
    }

    private void sendEmailMessage(String subject, String template, Map<String, Object> fields, String email) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Context context = new Context();
            context.setVariables(fields);
            String emailContent = templateEngine.process(template, context);

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(emailFrom);
            mimeMessageHelper.setText(emailContent, true);
            emailSender.send(message);
        } catch (Exception e) {
            log.info("error sending mail to {} with subject: {}", email, subject);
        }
    }
}
