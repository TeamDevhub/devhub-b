package teamdevhub.devhub.core.auth.domain;

import lombok.Getter;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.time.LocalDateTime;

@Getter
public class EmailUserCredential {

    private final String userGuid;
    private final String email;
    private String password;
    private final UserRole userRole;
    private LocalDateTime lastLoginDate;

    public EmailUserCredential(
            String userGuid,
            String email,
            String password,
            UserRole userRole,
            LocalDateTime lastLoginDate
    ) {
        this.userGuid = userGuid;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.lastLoginDate = lastLoginDate;
    }

    public void verifyPassword(boolean matches) {
        if (!matches) {
            throw DomainRuleException.of(ErrorCode.USER_PASSWORD_FAIL);
        }
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void markLoginSuccess() {
        this.lastLoginDate = LocalDateTime.now();
    }
}