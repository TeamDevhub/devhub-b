package teamdevhub.devhub.outbound.auth.infrastructure.oauth;

import lombok.Builder;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Builder
public record OAuthUser(String oAuthId, VerificationProvider verificationProvider, String email) {}
