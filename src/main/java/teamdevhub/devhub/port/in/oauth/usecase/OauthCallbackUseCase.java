package teamdevhub.devhub.port.in.oauth.usecase;

import teamdevhub.devhub.adapter.in.auth.dto.response.OauthCallbackResponseDto;
import teamdevhub.devhub.common.enums.VerificationProvider;

public interface OauthCallbackUseCase {

    String createAuthorizationUrl(String provider);
    OauthCallbackResponseDto handleOAuthCallback(VerificationProvider verificationProvider, String code);
}
