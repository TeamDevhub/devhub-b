package teamdevhub.devhub.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthResolveUseCase;
import teamdevhub.devhub.port.out.provider.TokenParseProvider;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class OauthResolveService implements OauthResolveUseCase {

    private final TokenParseProvider tokenParseProvider;
    private final UserRepository userRepository;

    @Override
    public OauthUserResult findOrRequireSignup(OauthUser oauthUser) {
        return userRepository
                .findByOAuth(oauthUser.verificationProvider(), oauthUser.oauthId())
                .map(OauthUserResult::success)
                .orElseGet(OauthUserResult::requiresSignup);
    }

    @Override
    public OauthUser extractOauthUser(SignupOauthUserCommand signupOauthUserCommand) {
        TempTokenInfo tempTokenInfo = tokenParseProvider.getTempTokenInfo(signupOauthUserCommand.tempToken());
        return new OauthUser(tempTokenInfo.oauthId(), tempTokenInfo.verificationProvider(), tempTokenInfo.email());
    }
}
