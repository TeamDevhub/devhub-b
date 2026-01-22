package teamdevhub.devhub.domain.auth.vo.user;

import lombok.Builder;
import teamdevhub.devhub.common.enums.VerificationProvider;

@Builder
public record OauthUser(String oauthId, VerificationProvider verificationProvider, String email) {}
