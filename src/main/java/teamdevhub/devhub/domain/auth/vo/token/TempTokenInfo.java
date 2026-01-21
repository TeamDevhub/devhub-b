package teamdevhub.devhub.domain.auth.vo.token;

import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.common.enums.SignupStatus;

public record TempTokenInfo(String oauthId, TokenType tokenType, SignupStatus signupStatus, VerificationProvider verificationProvider, String email) {}
