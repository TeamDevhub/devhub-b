package teamdevhub.devhub.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.time.LocalDateTime;

@Getter
public class OAuthUserCredential {

    private final String userGuid;
    private final String oauthId;
    private final String email;
    private final VerificationProvider verificationProvider;

    private LocalDateTime lastLoginDate;

    @Builder
    public OAuthUserCredential(
            String userGuid,
            String oauthId,
            String email,
            VerificationProvider verificationProvider,
            LocalDateTime lastLoginDate
    ) {
        this.userGuid = userGuid;
        this.oauthId = oauthId;
        this.email = email;
        this.verificationProvider = verificationProvider;
        this.lastLoginDate = lastLoginDate;
    }

    public void markLoginSuccess() {
        this.lastLoginDate = LocalDateTime.now();
    }
}
