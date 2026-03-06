package teamdevhub.devhub.core.user.port.in.command;

import lombok.Builder;

@Builder
public record UpdatePasswordCommand(String userGuid,
                                    String currentPassword,
                                    String newPassword) {
}
