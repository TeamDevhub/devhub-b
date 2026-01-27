package teamdevhub.devhub.core.auth.domain.vo.user;

import lombok.Builder;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Builder
public record OauthUser(String oauthId, VerificationProvider verificationProvider, String email) {}
