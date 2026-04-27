package teamdevhub.devhub.core.auth.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.core.user.port.in.command.UpdatePasswordCommand;
import teamdevhub.devhub.core.user.port.in.usecase.UserLoginUseCase;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthFacade {

    private final UserCredentialUseCase userCredentialUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final UserLoginUseCase userLoginUseCase;

    public AuthResult login(LoginCommand loginCommand) {
        AuthenticatedUser authenticatedUser = userCredentialUseCase.authenticate(loginCommand);
        userLoginUseCase.validateLoginUser(authenticatedUser.userGuid());
        AuthResult authResult = authenticationUseCase.login(authenticatedUser);
        userLoginUseCase.updateLastLoginDateTime(authenticatedUser.userGuid());
        return authResult;
    }

    public AuthResult reissueAccessToken(String token) {
        AuthenticatedUser authenticatedUser = userCredentialUseCase.getUserForReissue(token);
        return authenticationUseCase.reissueAccessToken(authenticatedUser);
    }

    public void logout(String userGuid) {
        authenticationUseCase.revoke(userGuid);
    }

    public void updatePassword(UpdatePasswordCommand updatePasswordCommand) {
        userCredentialUseCase.updatePassword(updatePasswordCommand);
    }
}
