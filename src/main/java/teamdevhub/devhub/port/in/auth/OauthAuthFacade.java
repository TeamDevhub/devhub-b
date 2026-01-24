package teamdevhub.devhub.port.in.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthAuthResponseDto;
import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.in.oauth.usecase.OauthAuthenticationUseCase;
import teamdevhub.devhub.port.in.oauth.usecase.OauthResolveUseCase;

@Service
@RequiredArgsConstructor
public class OauthAuthFacade {

    private final OauthAuthenticationUseCase oauthAuthenticationUseCase;
    private final OauthResolveUseCase oauthResolveUseCase;
    private final AuthenticationUseCase authenticationUseCase;

    public String createOAuthAuthorizationUrl(String provider) {
        return oauthAuthenticationUseCase.createAuthorizationUrl(provider);
    }

    public OauthAuthResponseDto handleOAuthCallback(String provider, String code) {
        OauthUser oauthUser = oauthAuthenticationUseCase.handleOAuthCallback(VerificationProvider.from(provider), code);
        OauthUserResult oauthUserResult = oauthResolveUseCase.findOrRequireSignup(oauthUser);

        if (oauthUserResult.loginAvailable()) {
            LoginResponseDto loginResponseDto = authenticationUseCase.login(oauthUserResult.authenticatedUser());
            return OauthAuthResponseDto.loggedIn(loginResponseDto);
        }

        String tempToken = oauthAuthenticationUseCase.issueTempToken(oauthUser);
        return OauthAuthResponseDto.requiresSignup(tempToken);
    }
}
