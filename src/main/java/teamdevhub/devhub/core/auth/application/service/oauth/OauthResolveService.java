package teamdevhub.devhub.core.auth.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.domain.vo.OAuthCredential;
import teamdevhub.devhub.core.auth.port.out.OauthCredentialRepository;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.outbound.auth.infrastructure.token.vo.TempTokenInfo;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.oauth.OauthResolveUseCase;
import teamdevhub.devhub.core.auth.port.out.token.TokenParseProvider;
import teamdevhub.devhub.core.user.port.out.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class OauthResolveService implements OauthResolveUseCase {

    private final TokenParseProvider tokenParseProvider;
    private final OauthCredentialRepository oauthCredentialRepository;
    private final UserRepository userRepository;

    @Override
    public OauthUserResult findOrRequireSignup(OauthUser oauthUser) {

        return oauthCredentialRepository
                .findByProviderAndOauthId(
                        oauthUser.verificationProvider(),
                        oauthUser.oauthId()
                )
                .map(this::toAuthenticatedUser)
                .map(OauthUserResult::success)
                .orElseGet(OauthUserResult::requiresSignup);
    }

    @Override
    public OauthUser extractOauthUser(SignupOauthUserCommand signupOauthUserCommand) {
        TempTokenInfo tempTokenInfo = tokenParseProvider.getTempTokenInfo(signupOauthUserCommand.tempToken());
        return new OauthUser(tempTokenInfo.oauthId(), tempTokenInfo.verificationProvider(), tempTokenInfo.email());
    }

    private AuthenticatedUser toAuthenticatedUser(OAuthCredential oAuthCredential) {

        User user = userRepository.findByUserGuid(oAuthCredential.userGuid());

        return AuthenticatedUser.of(
                user.getUserGuid(),
                oAuthCredential.provider() + "_" + oAuthCredential.oauthId(),
                user.getUserRole()
        );
    }
}
