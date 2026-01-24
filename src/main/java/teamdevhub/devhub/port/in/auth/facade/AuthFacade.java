package teamdevhub.devhub.port.in.auth.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticatedUserUseCase;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.in.user.usecase.UserLoginUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthenticatedUserUseCase authenticatedUserUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final UserLoginUseCase userLoginUseCase;

    public LoginResponseDto login(LoginCommand loginCommand) {
        AuthenticatedUser authenticatedUser = authenticatedUserUseCase.authenticate(loginCommand);
        LoginResponseDto loginResponseDto = authenticationUseCase.login(authenticatedUser);
        userLoginUseCase.updateLastLoginDateTime(authenticatedUser.userGuid());
        return loginResponseDto;
    }

    public TokenResponseDto reissueAccessToken(String token) {
        AuthenticatedUser authenticatedUser = authenticatedUserUseCase.getUserForReissue(token);
        return authenticationUseCase.reissueAccessToken(authenticatedUser);
    }

    public void logout(String userGuid) {
        authenticationUseCase.revoke(userGuid);
    }
}
