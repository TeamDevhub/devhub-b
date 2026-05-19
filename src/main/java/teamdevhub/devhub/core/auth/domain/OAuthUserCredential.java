package teamdevhub.devhub.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Getter
public class OAuthUserCredential {

    private final String userGuid;
    private final String oauthId;
    private final VerificationProvider verificationProvider;
    private final UserRole userRole;

    @Builder
    public OAuthUserCredential(
            String userGuid,
            String oauthId,
            VerificationProvider verificationProvider,
            UserRole userRole
    ) {
        this.userGuid = userGuid;
        this.oauthId = oauthId;
        this.verificationProvider = verificationProvider;
        this.userRole = userRole;
    }

    public static OAuthUserCredential of(
            String userGuid,
            String oauthId,
            VerificationProvider verificationProvider,
            UserRole userRole
    ) {
        return OAuthUserCredential.builder()
                .userGuid(userGuid)
                .oauthId(oauthId)
                .verificationProvider(verificationProvider)
                .userRole(userRole)
                .build();
    }
}
