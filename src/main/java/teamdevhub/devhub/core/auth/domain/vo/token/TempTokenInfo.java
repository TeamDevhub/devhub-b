package teamdevhub.devhub.core.auth.domain.vo.token;

import lombok.Builder;
import teamdevhub.devhub.shared.enums.TokenType;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Builder
public record TempTokenInfo(String oauthId, TokenType tokenType, VerificationProvider verificationProvider, String email) {}
