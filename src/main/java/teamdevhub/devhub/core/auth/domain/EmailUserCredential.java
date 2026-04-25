package teamdevhub.devhub.core.auth.domain;

import lombok.Getter;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Getter
public class EmailUserCredential {

    private final String userGuid;
    private final String email;
    private String password;
    private final UserRole userRole;

    public EmailUserCredential(
            String userGuid,
            String email,
            String password,
            UserRole userRole
    ) {
        this.userGuid = userGuid;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public void verifyPassword(boolean matches) {
        if (!matches) {
            throw DomainRuleException.of(ErrorCode.USER_PASSWORD_FAIL);
        }
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public AuthenticatedUser toAuthenticatedUser() {
        return AuthenticatedUser.of(userGuid, email, userRole);
    }
}