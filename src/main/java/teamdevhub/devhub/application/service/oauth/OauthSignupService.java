package teamdevhub.devhub.application.service.oauth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthSignupUseCase;
import teamdevhub.devhub.port.out.provider.TokenParseProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class OauthSignupService implements OauthSignupUseCase {

    private final TokenParseProvider tokenParseProvider;

    @Override
    public OauthUser signupWithOauth(SignupOauthUserCommand signupOauthUserCommand) {
        TempTokenInfo tempTokenInfo = tokenParseProvider.getTempTokenInfo(signupOauthUserCommand.tempToken());
        return new OauthUser(tempTokenInfo.oauthId(), tempTokenInfo.verificationProvider(), tempTokenInfo.email());
    }
}
