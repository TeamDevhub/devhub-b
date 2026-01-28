package teamdevhub.devhub.core.auth.domain.vo.token;

import lombok.Builder;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Builder
public record TempTokenInfo(String oauthId, VerificationProvider verificationProvider, String email) {}
