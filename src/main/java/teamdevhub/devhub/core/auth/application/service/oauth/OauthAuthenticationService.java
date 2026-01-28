package teamdevhub.devhub.core.auth.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.selector.oauth.OauthClientSelector;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.OauthUser;
import teamdevhub.devhub.core.auth.port.in.usecase.oauth.OauthAuthenticationUseCase;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;
import teamdevhub.devhub.core.auth.port.out.token.TokenIssueProvider;

@Service
@Transactional
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
    public OauthUser handleOAuthCallback(VerificationProvider verificationProvider, String authorizationCode) {
        OauthClient oauthClient = oauthClientSelector.select(verificationProvider);
        return oauthClient.fetchUser(authorizationCode);
    }

    @Override
    public String issueTempToken(OauthUser oauthUser) {
        return tokenIssueProvider.createTempToken(oauthUser.oauthId(), oauthUser.verificationProvider(), oauthUser.email());
    }
}