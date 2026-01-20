package teamdevhub.devhub.application.service.oauth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthSignupUseCase;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;
import teamdevhub.devhub.port.out.provider.TokenParseProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class OauthSignupService implements OauthSignupUseCase {

    private final TokenParseProvider tokenParseProvider;
    private final UserSignupUseCase userSignupUseCase;

    @Override
    public String signupWithOauth(OauthSignupCommand oauthSignupCommand) {
        TempTokenInfo tempTokenInfo = tokenParseProvider.getTempTokenInfo(oauthSignupCommand.tempToken());
        OauthUser oauthUser = new OauthUser(tempTokenInfo.oauthId(), tempTokenInfo.verificationProvider(), tempTokenInfo.email());
        userSignupUseCase.signupWithOauth(oauthSignupCommand, oauthUser);
        return oauthSignupCommand.tempToken();
    }
}
