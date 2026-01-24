package teamdevhub.devhub.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.application.selector.oauth.OauthClientSelector;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.oauth.usecase.OauthAuthenticationUseCase;
import teamdevhub.devhub.port.out.oauth.OauthClient;
import teamdevhub.devhub.port.out.provider.TokenIssueProvider;

@Service
@RequiredArgsConstructor
public class OauthAuthenticationService implements OauthAuthenticationUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final OauthClientSelector oauthClientSelector;

    @Override
    public String createAuthorizationUrl(String provider) {
        VerificationProvider verificationProvider = VerificationProvider.from(provider);
        OauthClient oauthClient = oauthClientSelector.select(verificationProvider);
        return oauthClient.getAuthorizationUrl();
    }

    @Override
    public String handleOAuthCallback(VerificationProvider verificationProvider, String authorizationCode) {
        OauthClient oauthClient = oauthClientSelector.select(verificationProvider);
        OauthUser oauthUser = oauthClient.fetchUser(authorizationCode);
        return tokenIssueProvider.createTempToken(oauthUser.oauthId(), verificationProvider, oauthUser.email());
    }
}