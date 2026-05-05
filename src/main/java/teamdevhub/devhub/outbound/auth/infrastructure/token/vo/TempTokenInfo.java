package teamdevhub.devhub.outbound.auth.infrastructure.token.vo;

import lombok.Builder;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Builder
public record TempTokenInfo(String oAuthId, VerificationProvider verificationProvider, String email) {}
