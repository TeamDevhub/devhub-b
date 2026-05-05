package teamdevhub.devhub.core.user.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.application.service.oauth.vo.OAuthResult;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.core.terms.port.in.usecase.TermsUseCase;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.port.in.usecase.UserLoginUseCase;
import teamdevhub.devhub.core.user.port.in.usecase.UserSignupUseCase;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.oauth.OAuthResolveUseCase;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.verification.VerificationUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignupFacade {

    private final UserSignupUseCase userSignupUseCase;
    private final TermsUseCase termsUseCase;
    private final OAuthResolveUseCase oauthResolveUseCase;
    private final UserCredentialUseCase userCredentialUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final VerificationUseCase verificationUseCase;
    private final UserLoginUseCase userLoginUseCase;

    public AuthResult signup(SignupUserCommand signupUserCommand) {
        verificationUseCase.assertAllowed(signupUserCommand.verificationTarget());
        String userGuid = userCredentialUseCase.signupEmailUser(signupUserCommand);
        userSignupUseCase.saveEmailUserInfo(signupUserCommand, userGuid);
        termsUseCase.saveTermsAgreement(signupUserCommand.toAgreeTermsCommand(userGuid));
        verificationUseCase.consume(signupUserCommand.verificationTarget());
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(userGuid, signupUserCommand.email(), UserRole.USER);
        userLoginUseCase.updateLastLoginDateTime(userGuid);
        return authenticationUseCase.login(authenticatedUser);
    }

    public OAuthResult signupWithOAuth(SignupOAuthUserCommand signupOAuthUserCommand) {
        OAuthUser oAuthUser = oauthResolveUseCase.extractOAuthUser(signupOAuthUserCommand);
        AuthenticatedUser authenticatedUser = userCredentialUseCase.signupOAuthUser(oAuthUser);
        userSignupUseCase.saveOAuthUserInfo(signupOAuthUserCommand, authenticatedUser.userGuid());
        termsUseCase.saveTermsAgreement(signupOAuthUserCommand.toAgreeTermsCommand(authenticatedUser.userGuid()));
        AuthResult authResult = authenticationUseCase.login(authenticatedUser);
        return OAuthResult.loggedIn(authResult);
    }
}
