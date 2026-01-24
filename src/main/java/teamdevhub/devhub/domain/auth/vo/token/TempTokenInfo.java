package teamdevhub.devhub.domain.auth.vo.token;

import lombok.Builder;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.enums.VerificationProvider;

@Builder
public record TempTokenInfo(String oauthId, TokenType tokenType, VerificationProvider verificationProvider, String email) {}
