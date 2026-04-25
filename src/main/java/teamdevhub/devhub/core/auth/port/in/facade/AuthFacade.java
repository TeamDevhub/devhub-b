package teamdevhub.devhub.core.auth.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.core.user.port.in.usecase.UserLoginUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthFacade {

    private final UserCredentialUseCase userCredentialUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final UserLoginUseCase userLoginUseCase;

    public AuthResult login(LoginCommand loginCommand) {
        UserCredential userCredential = userCredentialUseCase.authenticate(loginCommand);
        AuthResult authResult = authenticationUseCase.login(userCredential);
        userLoginUseCase.updateLastLoginDateTime(userCredential.userGuid());
        return authResult;
    }

    public AuthResult reissueAccessToken(String token) {
        UserCredential userCredential = userCredentialUseCase.getUserForReissue(token);
        return authenticationUseCase.reissueAccessToken(userCredential);
    }

    public void logout(String userGuid) {
        authenticationUseCase.revoke(userGuid);
    }
}
