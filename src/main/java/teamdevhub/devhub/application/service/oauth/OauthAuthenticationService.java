package teamdevhub.devhub.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.application.selector.oauth.OauthClientSelector;
import teamdevhub.devhub.application.service.oauth.vo.OauthCallbackResult;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.oauth.usecase.OauthAuthenticationUseCase;
import teamdevhub.devhub.port.out.oauth.OauthClient;
import teamdevhub.devhub.port.out.provider.TokenIssueProvider;
import teamdevhub.devhub.port.out.user.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OauthAuthenticationService implements OauthAuthenticationUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final OauthClientSelector oauthClientSelector;
    private final UserRepository userRepository;

    @Override
    public String createAuthorizationUrl(String provider) {
        VerificationProvider verificationProvider = VerificationProvider.from(provider);
        OauthClient oauthClient = oauthClientSelector.select(verificationProvider);
        return oauthClient.getAuthorizationUrl();
    }

    @Override
    public OauthCallbackResult handleOAuthCallback(VerificationProvider verificationProvider, String authorizationCode) {
        OauthClient oauthClient = oauthClientSelector.select(verificationProvider);
        OauthUser oauthUser = oauthClient.fetchUser(authorizationCode);

        Optional<AuthenticatedUser> optionalUser = userRepository.findByOAuth(oauthUser.verificationProvider(), oauthUser.oauthId());

        if (optionalUser.isPresent()) {
            String tempToken = tokenIssueProvider.createTempToken(oauthUser.oauthId(), SignupStatus.COMPLETED, verificationProvider, oauthUser.email());
            return OauthCallbackResult.existedUser(tempToken);
        } else {
            String tempToken = tokenIssueProvider.createTempToken(oauthUser.oauthId(), SignupStatus.PENDING, verificationProvider, oauthUser.email());
            return OauthCallbackResult.requiresSignupUser(tempToken);
        }
    }
}