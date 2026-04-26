package teamdevhub.devhub.core.auth.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.outbound.auth.infrastructure.token.vo.TempTokenInfo;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.oauth.OauthResolveUseCase;
import teamdevhub.devhub.core.auth.port.out.token.TokenParseProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class OauthResolveService implements OauthResolveUseCase {

    private final TokenParseProvider tokenParseProvider;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public OauthUserResult findOrRequireSignup(OauthUser oauthUser) {

        return userCredentialRepository
                .findOAuthUserCredentialByOAuth(
                        oauthUser.verificationProvider(),
                        oauthUser.oauthId()
                )
                .map(OauthUserResult::success)
                .orElseGet(OauthUserResult::requiresSignup);
    }

    @Override
    public OauthUser extractOauthUser(SignupOauthUserCommand signupOauthUserCommand) {
        TempTokenInfo tempTokenInfo = tokenParseProvider.getTempTokenInfo(signupOauthUserCommand.tempToken());
        return new OauthUser(tempTokenInfo.oauthId(), tempTokenInfo.verificationProvider(), tempTokenInfo.email());
    }
}
