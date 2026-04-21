package teamdevhub.devhub.core.auth.domain;

import lombok.Builder;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

@Builder
public record UserCredential(String userGuid, String loginId, UserRole userRole) {

    public static UserCredential of(String userGuid, String loginId, UserRole userRole) {
        return new UserCredential(userGuid, loginId, userRole);
    }
}
