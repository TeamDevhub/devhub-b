package teamdevhub.devhub.core.auth.domain.vo;

import teamdevhub.devhub.shared.enums.VerificationProvider;

public record OAuthCredential(
        String userGuid,
        VerificationProvider provider,
        String oauthId
) {}
