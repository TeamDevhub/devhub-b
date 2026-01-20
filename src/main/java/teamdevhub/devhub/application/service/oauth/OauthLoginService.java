package teamdevhub.devhub.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.in.oauth.command.OauthLoginCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthLoginUseCase;
import teamdevhub.devhub.port.out.provider.TokenParseProvider;
import teamdevhub.devhub.port.out.user.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OauthLoginService implements OauthLoginUseCase {

    private final TokenParseProvider tokenParseProvider;
    private final AuthenticationUseCase authenticationUseCase;
    private final UserRepository userRepository;

    @Override
    public LoginResponseDto loginWithOauth(OauthLoginCommand oauthLoginCommand) {
        TempTokenInfo tempTokenInfo = tokenParseProvider.getTempTokenInfo(oauthLoginCommand.tempToken());

        VerificationProvider verificationProvider = tempTokenInfo.verificationProvider();
        String oauthId = tempTokenInfo.oauthId();
        Optional<AuthenticatedUser> optionalAuthenticatedUser = userRepository.findByOAuth(verificationProvider, oauthId);

        if (optionalAuthenticatedUser.isPresent()) {
            AuthenticatedUser authenticatedUser = optionalAuthenticatedUser.get();
            return authenticationUseCase.loginWithOauth(authenticatedUser);
        } else {
            return LoginResponseDto.notExistedOAuthUser(oauthLoginCommand.tempToken(), SignupStatus.PENDING);
        }
    }
}
