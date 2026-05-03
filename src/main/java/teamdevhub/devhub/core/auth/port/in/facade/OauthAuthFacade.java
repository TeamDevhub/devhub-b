package teamdevhub.devhub.core.auth.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthResult;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthorizationResult;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthUserResult;
import teamdevhub.devhub.core.user.port.in.usecase.UserLoginUseCase;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.core.auth.port.in.usecase.oauth.OauthAuthenticationUseCase;
import teamdevhub.devhub.core.auth.port.in.usecase.oauth.OauthResolveUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class OauthAuthFacade {

    private final OauthAuthenticationUseCase oauthAuthenticationUseCase;
    private final OauthResolveUseCase oauthResolveUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final UserLoginUseCase userLoginUseCase;

    public OauthAuthorizationResult createOAuthAuthorizationUrl(String provider) {
        return oauthAuthenticationUseCase.createAuthorizationUrl(provider);
    }

    public OauthAuthResult handleOAuthCallback(String provider, String code) {
        OauthUser oauthUser = oauthAuthenticationUseCase.handleOAuthCallback(VerificationProvider.from(provider), code);
        OauthUserResult oauthUserResult = oauthResolveUseCase.findOrRequireSignup(oauthUser);

        if (oauthUserResult.loginAvailable()) {
            userLoginUseCase.validateLoginUser(oauthUserResult.authenticatedUser().userGuid());
            AuthResult authResult = authenticationUseCase.login(oauthUserResult.authenticatedUser());
            userLoginUseCase.updateLastLoginDateTime(oauthUserResult.authenticatedUser().userGuid());
            return OauthAuthResult.loggedIn(authResult);
        }

        String tempToken = oauthAuthenticationUseCase.issueTempToken(oauthUser);
        return OauthAuthResult.requiresSignup(tempToken);
    }
}
