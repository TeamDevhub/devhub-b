package teamdevhub.devhub.domain.user.vo.user;

import lombok.Builder;

@Builder
public record UpdateUserCommand(String username, String introduction) {
}
