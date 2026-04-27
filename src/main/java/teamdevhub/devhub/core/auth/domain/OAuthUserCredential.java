package teamdevhub.devhub.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.time.LocalDateTime;

@Getter
public class OAuthUserCredential {

    private final String userGuid;
    private final String oauthId;
    private final VerificationProvider verificationProvider;
    private final UserRole userRole;
    private LocalDateTime lastLoginDate;

    @Builder
    public OAuthUserCredential(
            String userGuid,
            String oauthId,
            VerificationProvider verificationProvider,
            UserRole userRole,
            LocalDateTime lastLoginDate
    ) {
        this.userGuid = userGuid;
        this.oauthId = oauthId;
        this.verificationProvider = verificationProvider;
        this.userRole = userRole;
        this.lastLoginDate = lastLoginDate;
    }

    public void markLoginSuccess() {
        this.lastLoginDate = LocalDateTime.now();
    }
}
