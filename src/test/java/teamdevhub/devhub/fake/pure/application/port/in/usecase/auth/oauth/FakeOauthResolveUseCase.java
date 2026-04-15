package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.oauth;

import lombok.Setter;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthUserResult;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.oauth.OauthResolveUseCase;

import static teamdevhub.devhub.constant.UserTestConstant.*;

@Setter
public class FakeOauthResolveUseCase implements OauthResolveUseCase {

    private OauthUser lastOauthUser;
    private OauthUserResult oauthUserResult;

    @Override
    public OauthUserResult findOrRequireSignup(OauthUser oauthUser) {
        this.lastOauthUser = oauthUser;

        if (oauthUserResult.loginAvailable()) {
            AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                    .userGuid(TEST_USER_GUID_1)
                    .email(TEST_EMAIL_1)
                    .userRole(UserRole.USER)
                    .build();

            return OauthUserResult.success(authenticatedUser);
        }

        return OauthUserResult.requiresSignup();
    }

    @Override
    public OauthUser extractOauthUser(SignupOauthUserCommand signupOauthUserCommand) {
        return new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
    }

    public OauthUser getLastOauthUser() {
        return lastOauthUser;
    }
}