package teamdevhub.devhub.domain.auth.vo.token;

import lombok.Builder;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.common.enums.SignupStatus;

@Builder
public record TempTokenInfo(String oauthId, TokenType tokenType, SignupStatus signupStatus, VerificationProvider verificationProvider, String email) {}
