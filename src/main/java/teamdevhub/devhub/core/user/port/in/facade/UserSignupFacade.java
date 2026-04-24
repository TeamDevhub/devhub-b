package teamdevhub.devhub.core.user.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthResult;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.core.terms.port.in.usecase.TermsUseCase;
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.usecase.UserSignupUseCase;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.oauth.OauthResolveUseCase;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.verification.VerificationUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignupFacade {

    private final UserSignupUseCase userSignupUseCase;
    private final TermsUseCase termsUseCase;
    private final OauthResolveUseCase oauthResolveUseCase;
    private final UserCredentialUseCase userCredentialUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final VerificationUseCase verificationUseCase;

    public void signup(SignupUserCommand signupUserCommand) {
        verificationUseCase.assertAllowed(signupUserCommand.verificationTarget());
        String userGuid = userCredentialUseCase.signupEmailUser(signupUserCommand);
        userSignupUseCase.saveEmailUserInfo(signupUserCommand, userGuid);
        termsUseCase.saveTermsAgreement(signupUserCommand.toAgreeTermsCommand(userGuid));
        verificationUseCase.consume(signupUserCommand.verificationTarget());
    }

    public OauthAuthResult signupWithOauth(SignupOauthUserCommand signupOauthUserCommand) {
        OauthUser oauthUser = oauthResolveUseCase.extractOauthUser(signupOauthUserCommand);
        UserCredential userCredential = userCredentialUseCase.signupOAuthUser(oauthUser);
        userSignupUseCase.saveOAuthUserInfo(signupOauthUserCommand, oauthUser, userCredential.userGuid());
        termsUseCase.saveTermsAgreement(signupOauthUserCommand.toAgreeTermsCommand(userCredential.userGuid()));
        AuthResult authResult = authenticationUseCase.login(userCredential);
        return OauthAuthResult.loggedIn(authResult);
    }
}
