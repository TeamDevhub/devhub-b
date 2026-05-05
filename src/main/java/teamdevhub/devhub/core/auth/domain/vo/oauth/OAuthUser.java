package teamdevhub.devhub.core.auth.domain.vo.oauth;

import lombok.Builder;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Builder
public record OAuthUser(String oauthId, VerificationProvider verificationProvider, String email) {}
