package teamdevhub.devhub.port.in.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthAuthResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticatedUserUseCase;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.in.oauth.command.ResolveOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthResolveUseCase;
import teamdevhub.devhub.port.in.user.usecase.UserLoginUseCase;
import teamdevhub.devhub.port.out.auth.AuthenticatedUserResolver;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthenticatedUserUseCase authenticatedUserUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final OauthResolveUseCase oauthResolveUseCase;
    private final UserLoginUseCase userLoginUseCase;
    private final AuthenticatedUserResolver authenticatedUserResolver;

    public LoginResponseDto login(LoginCommand loginCommand) {
        AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser(loginCommand.email(), loginCommand.password());
        LoginResponseDto loginResponseDto = authenticationUseCase.login(authenticatedUser);
        userLoginUseCase.updateLastLoginDateTime(authenticatedUser.userGuid());
        return loginResponseDto;
    }

    public OauthAuthResponseDto loginWithOauth(ResolveOauthUserCommand resolveOauthUserCommand) {
        OauthUserResult oauthUserResult = oauthResolveUseCase.findOrRequireSignup(resolveOauthUserCommand.tempToken());

        if (oauthUserResult.loginAvailable()) {
            LoginResponseDto loginResponseDto = authenticationUseCase.loginWithOauth(oauthUserResult.authenticatedUser());
            return OauthAuthResponseDto.loggedIn(loginResponseDto);
        }

        return OauthAuthResponseDto.requiresSignup(oauthUserResult.tempToken());
    }

    public TokenResponseDto reissueAccessToken(String token) {
        AuthenticatedUser authenticatedUser = authenticatedUserUseCase.getUserForReissue(token);
        return authenticationUseCase.reissueAccessToken(authenticatedUser);
    }

    public void logout(String userGuid) {
        authenticationUseCase.revoke(userGuid);
    }
}
