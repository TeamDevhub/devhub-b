package teamdevhub.devhub.port.in.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthAuthResponseDto;
import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.in.oauth.command.ResolveOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthResolveUseCase;
import teamdevhub.devhub.port.in.oauth.usecase.OauthSignupUseCase;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;
import teamdevhub.devhub.port.in.verification.usecase.VerificationUseCase;

@Service
@RequiredArgsConstructor
public class UserSignupFacade {

    private final UserSignupUseCase userSignupUseCase;
    private final OauthResolveUseCase oauthResolveUseCase;
    private final OauthSignupUseCase oauthSignupUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final VerificationUseCase verificationUseCase;

    public User signup(SignupUserCommand signupUserCommand) {
        verificationUseCase.assertAllowed(signupUserCommand.verificationTarget());
        User savedUser = userSignupUseCase.signup(signupUserCommand);
        verificationUseCase.consume(signupUserCommand.verificationTarget());
        return savedUser;
    }

    /**
     * 점검 필요
     * @param signupOauthUserCommand
     * @return
     */
    public OauthAuthResponseDto signupWithOauth(SignupOauthUserCommand signupOauthUserCommand) {
        OauthUser oauthUser = oauthSignupUseCase.signupWithOauth(signupOauthUserCommand);
        userSignupUseCase.signupWithOauth(signupOauthUserCommand, oauthUser);

        ResolveOauthUserCommand resolveOauthUserCommand = new ResolveOauthUserCommand(signupOauthUserCommand.tempToken());
        OauthUserResult oauthUserResult = oauthResolveUseCase.resolveOauthUser(resolveOauthUserCommand);
        LoginResponseDto loginResponseDto = authenticationUseCase.loginWithOauth(oauthUserResult.authenticatedUser());
        return OauthAuthResponseDto.loggedIn(loginResponseDto);
    }
}
