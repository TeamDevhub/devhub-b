package teamdevhub.devhub.domain.auth.vo.user;

import lombok.Builder;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.domain.user.UserRole;

@Builder
public record AuthenticatedUser(String userGuid, SignupStatus signupStatus, String email, String password, UserRole userRole) {

    public static AuthenticatedUser of(String userGuid, SignupStatus signupStatus, String email, String password, UserRole userRole) {
        return new AuthenticatedUser(userGuid, signupStatus, email, password, userRole);
    }
}
