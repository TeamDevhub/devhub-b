package teamdevhub.devhub.fake.pure.usecase.oauth;

import teamdevhub.devhub.adapter.in.auth.dto.response.OauthCallbackResponseDto;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.port.in.oauth.usecase.OauthCallbackUseCase;

public class FakeOauthCallbackUseCase implements OauthCallbackUseCase {

    @Override
    public String createAuthorizationUrl(String provider) {
        return "https://oauth.test/" + provider;
    }

    @Override
    public OauthCallbackResponseDto handleOAuthCallback(VerificationProvider verificationProvider, String code) {
        return OauthCallbackResponseDto.existedUser("TEMP_TOKEN");
    }
}
