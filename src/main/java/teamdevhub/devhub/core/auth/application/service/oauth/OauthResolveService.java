package teamdevhub.devhub.core.auth.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.vo.OauthUserResult;
import teamdevhub.devhub.core.auth.domain.vo.token.TempTokenInfo;
import teamdevhub.devhub.core.auth.domain.vo.user.OauthUser;
import teamdevhub.devhub.core.auth.port.in.command.SignupOauthUserCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.OauthResolveUseCase;
import teamdevhub.devhub.core.common.provider.TokenParseProvider;
import teamdevhub.devhub.core.user.port.out.UserRepository;

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
