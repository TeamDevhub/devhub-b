package teamdevhub.devhub.core.notification.port.in.command;

import lombok.Builder;
import teamdevhub.devhub.shared.enums.NotificationType;

import java.util.List;

@Builder
public record CreateNotificationCommand(
        String receiverId,
        NotificationType type,
        List<Object> messageArgs,
        String redirectTarget
) {}