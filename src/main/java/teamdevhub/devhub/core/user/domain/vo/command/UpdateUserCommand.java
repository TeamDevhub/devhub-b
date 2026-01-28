package teamdevhub.devhub.core.user.domain.vo.command;

import lombok.Builder;

@Builder
public record UpdateUserCommand(String username, String introduction) {
}
