package org.petmarket.notifications;

import org.petmarket.language.entity.Language;
import org.petmarket.users.entity.User;

import java.util.Map;

public interface NotificationService {

    void send(NotificationType type, Map<String, Object> fields, Language language, String email);
    void send(NotificationType type, Map<String, Object> fields, User user);
}
