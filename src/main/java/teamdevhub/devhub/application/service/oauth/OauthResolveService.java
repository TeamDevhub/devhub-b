package teamdevhub.devhub.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.port.in.oauth.command.ResolveOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthResolveUseCase;
import teamdevhub.devhub.port.out.provider.TokenParseProvider;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@RequiredArgsConstructor
public class OauthResolveService implements OauthResolveUseCase {

    private final TokenParseProvider tokenParseProvider;
    private final UserRepository userRepository;

    @Override
    public OauthUserResult resolveOauthUser(ResolveOauthUserCommand resolveOauthUserCommand) {
        TempTokenInfo tempTokenInfo = tokenParseProvider.getTempTokenInfo(resolveOauthUserCommand.tempToken());
        return userRepository
                .findByOAuth(tempTokenInfo.verificationProvider(), tempTokenInfo.oauthId())
                .map(OauthUserResult::success)
                .orElseGet(() -> OauthUserResult.requiresSignup(resolveOauthUserCommand.tempToken()));
    }
}
