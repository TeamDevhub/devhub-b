package teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo;

import lombok.Builder;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Builder
public record OauthUser(String oauthId, VerificationProvider verificationProvider, String email) {}
