package teamdevhub.devhub.port.in.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthAuthResponseDto;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthResolveUseCase;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;
import teamdevhub.devhub.port.in.verification.usecase.VerificationUseCase;

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

    public OauthAuthResponseDto signupWithOauth(SignupOauthUserCommand signupOauthUserCommand) {
        OauthUser oauthUser = oauthResolveUseCase.extractOauthUser(signupOauthUserCommand);
        User savedUser = userSignupUseCase.signupWithOauth(signupOauthUserCommand, oauthUser);

        AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                .userGuid(savedUser.getUserGuid())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .userRole(savedUser.getUserRole())
                .build();

        LoginResponseDto loginResponseDto = authenticationUseCase.login(authenticatedUser);
        return OauthAuthResponseDto.loggedIn(loginResponseDto);
    }
}
