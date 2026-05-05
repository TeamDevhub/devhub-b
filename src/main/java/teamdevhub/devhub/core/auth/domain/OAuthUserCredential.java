package teamdevhub.devhub.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Getter
public class OAuthUserCredential {

    private final String userGuid;
    private final String oAuthId;
    private final VerificationProvider verificationProvider;
    private final UserRole userRole;

    @Builder
    public OAuthUserCredential(
            String userGuid,
            String oAuthId,
            VerificationProvider verificationProvider,
            UserRole userRole
    ) {
        this.userGuid = userGuid;
        this.oAuthId = oAuthId;
        this.verificationProvider = verificationProvider;
        this.userRole = userRole;
    }
}
