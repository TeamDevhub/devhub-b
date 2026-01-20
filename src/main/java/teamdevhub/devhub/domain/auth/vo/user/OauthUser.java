package teamdevhub.devhub.domain.auth.vo.user;

import teamdevhub.devhub.common.enums.VerificationProvider;

public record OauthUser(String oauthId, VerificationProvider verificationProvider, String email) {}
