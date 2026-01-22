package teamdevhub.devhub.domain.user.vo.user;

import lombok.Builder;

@Builder
public record UserUpdateCommand(String username, String introduction) {
}
