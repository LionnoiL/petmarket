package org.petmarket.notifications;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${site.name}")
    private String siteName;

    @Override
    public void send(NotificationType type, Map<String, Object> fields, User user) {
        log.info("send message to {} with text: {}", user.getEmail(), fields.get("link"));

        fields.put("mailtoLink", "mailto:" + emailFrom);
        fields.put("email", emailFrom);
        fields.put("footer", "© " + Helper.getCurrentYear() + " " + siteName);

        String userLangCode = getUserLanguageCode(user);
        String templateName = getTemplateName(type, userLangCode);

        switch (type) {
            case RESET_PASSWORD -> sendEmailMessage(
                    translationService.getTranslate(TranslationMessages.PASSWORD_CHANGE_REQUEST, userLangCode),
                    templateName,
                    fields,
                    user
            );
            case CHANGE_PASSWORD, UPDATE_PASSWORD -> sendEmailMessage(
                    translationService.getTranslate(TranslationMessages.PASSWORD_CHANGED_SUCCESSFULLY, userLangCode),
                    templateName,
                    fields,
                    user
            );
            default -> log.info("error sending mail to {}", user.getEmail());
        }
    }

    private String getUserLanguageCode(User user) {
        if (user.getLanguage() == null) {
            return "ua";
        } else {
            return user.getLanguage().getLangCode();
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
            default -> log.info("no template assigned for to {}", type);
        }
        return result.toString();
    }

    private void sendEmailMessage(String subject, String template, Map<String, Object> fields, User user) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Context context = new Context();
            context.setVariables(fields);
            String emailContent = templateEngine.process(template, context);

            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(emailFrom);
            mimeMessageHelper.setText(emailContent, true);
            emailSender.send(message);
        } catch (Exception e) {
            log.info("error sending mail to {} with subject: {}", user.getEmail(), subject);
        }
    }
}
