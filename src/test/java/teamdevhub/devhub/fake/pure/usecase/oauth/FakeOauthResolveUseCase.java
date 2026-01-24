package teamdevhub.devhub.fake.pure.usecase.oauth;

import lombok.Setter;
import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthResolveUseCase;

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