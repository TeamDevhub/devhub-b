package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.oauth;

import lombok.Setter;
import teamdevhub.devhub.core.auth.application.service.oauth.vo.OAuthUserResult;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.oauth.OAuthResolveUseCase;

import static teamdevhub.devhub.constant.UserTestConstant.*;

@Setter
public class FakeOAuthResolveUseCase implements OAuthResolveUseCase {

    private OAuthUser lastOAuthUser;
    private OAuthUserResult oAuthUserResult;

    @Override
    public OAuthUserResult findOrRequireSignup(OAuthUser oauthUser) {
        this.lastOAuthUser = oauthUser;

        if (oAuthUserResult.loginAvailable()) {
            AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                    .userGuid(TEST_USER_GUID_1)
                    .loginId(TEST_EMAIL_1)
                    .userRole(UserRole.USER)
                    .build();

            return OAuthUserResult.success(authenticatedUser);
        }

        return OAuthUserResult.requiresSignup();
    }

    @Override
    public OAuthUser extractOAuthUser(SignupOAuthUserCommand signupOAuthUserCommand) {
        return new OAuthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
    }

    public OAuthUser getLastOAuthUser() {
        return lastOAuthUser;
    }
}