package teamdevhub.devhub.core.auth.domain.vo.user;

import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.shared.enums.VerificationProvider;

public record OAuthUserCredential(
        String userGuid,
        VerificationProvider provider,
        String oauthId,
        UserRole userRole
) {}
