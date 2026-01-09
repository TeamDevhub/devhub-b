package teamdevhub.devhub.port.out.mail;

import teamdevhub.devhub.common.enums.EmailTemplateType;

import java.util.Map;

public interface EmailNotificationSender {
    void send(String email, EmailTemplateType templateType, Map<String, Object> variables);
}