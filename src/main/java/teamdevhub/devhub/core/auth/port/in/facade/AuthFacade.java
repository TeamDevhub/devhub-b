package teamdevhub.devhub.core.auth.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticatedUserUseCase;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.core.user.port.in.usecase.UserLoginUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthenticatedUserUseCase authenticatedUserUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final UserLoginUseCase userLoginUseCase;

    public AuthResult login(LoginCommand loginCommand) {
        AuthenticatedUser authenticatedUser = authenticatedUserUseCase.authenticate(loginCommand);
        AuthResult authResult = authenticationUseCase.login(authenticatedUser);
        userLoginUseCase.updateLastLoginDateTime(authenticatedUser.userGuid());
        return authResult;
    }

    public AuthResult reissueAccessToken(String token) {
        AuthenticatedUser authenticatedUser = authenticatedUserUseCase.getUserForReissue(token);
        return authenticationUseCase.reissueAccessToken(authenticatedUser);
    }

    public void logout(String userGuid) {
        authenticationUseCase.revoke(userGuid);
    }
}
