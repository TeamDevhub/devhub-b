package teamdevhub.devhub.core.auth.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.selector.oauth.OAuthClientSelector;
import teamdevhub.devhub.core.auth.application.service.oauth.vo.OAuthAuthorizationResult;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.core.auth.port.in.usecase.oauth.OAuthAuthenticationUseCase;
import teamdevhub.devhub.core.auth.port.out.oauth.OAuthClient;
import teamdevhub.devhub.core.auth.port.out.token.TokenIssueProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthAuthenticationService implements OAuthAuthenticationUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final OAuthClientSelector oAuthClientSelector;
    private final IdentifierProvider identifierProvider;

    @Override
    public OAuthAuthorizationResult createAuthorizationUrl(String provider) {
        VerificationProvider verificationProvider = VerificationProvider.from(provider);
        OAuthClient oAuthClient = oAuthClientSelector.select(verificationProvider);
        String state = identifierProvider.generateIdentifier();
        String url = oAuthClient.getAuthorizationUrl(state);
        return OAuthAuthorizationResult.of(url, state);
    }

    @Override
    public OAuthUser handleOAuthCallback(VerificationProvider verificationProvider, String authorizationCode) {
        OAuthClient oAuthClient = oAuthClientSelector.select(verificationProvider);
        return oAuthClient.fetchUser(authorizationCode);
    }

    @Override
    public String issueTempToken(OAuthUser oAuthUser) {
        return tokenIssueProvider.createTempToken(oAuthUser.oAuthId(), oAuthUser.verificationProvider(), oAuthUser.email());
    }
}