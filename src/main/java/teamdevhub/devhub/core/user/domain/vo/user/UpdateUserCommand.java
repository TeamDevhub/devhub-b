package teamdevhub.devhub.core.user.domain.vo.user;

import lombok.Builder;

@Builder
public record UpdateUserCommand(String username, String introduction) {
}
