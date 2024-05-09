package org.petmarket.notifications;

import org.petmarket.users.entity.User;

import java.util.Map;

public interface NotificationService {
    void send(NotificationType type, Map<String, Object> fields, User user);
}
