package teamdevhub.devhub.core.user.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.vo.AuthResult;
import teamdevhub.devhub.core.auth.application.service.vo.OauthAuthResult;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.domain.vo.user.OauthUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.usecase.UserSignupUseCase;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.core.auth.port.in.command.SignupOauthUserCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.OauthResolveUseCase;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.VerificationUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignupFacade {

    private final UserSignupUseCase userSignupUseCase;
    private final OauthResolveUseCase oauthResolveUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final VerificationUseCase verificationUseCase;

    public void signup(SignupUserCommand signupUserCommand) {
        verificationUseCase.assertAllowed(signupUserCommand.verificationTarget());
        userSignupUseCase.signup(signupUserCommand);
        verificationUseCase.consume(signupUserCommand.verificationTarget());
    }

    public OauthAuthResult signupWithOauth(SignupOauthUserCommand signupOauthUserCommand) {
        OauthUser oauthUser = oauthResolveUseCase.extractOauthUser(signupOauthUserCommand);
        User savedUser = userSignupUseCase.signupWithOauth(signupOauthUserCommand, oauthUser);

        AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                .userGuid(savedUser.getUserGuid())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .userRole(savedUser.getUserRole())
                .build();

        AuthResult authResult = authenticationUseCase.login(authenticatedUser);
        return OauthAuthResult.loggedIn(authResult);
    }
}
