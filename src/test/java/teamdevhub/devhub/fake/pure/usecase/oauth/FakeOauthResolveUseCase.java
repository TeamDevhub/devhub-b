package teamdevhub.devhub.fake.pure.usecase.oauth;

import lombok.Setter;
import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.in.oauth.command.ResolveOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthResolveUseCase;

import static teamdevhub.devhub.constant.UserTestConstant.*;

@Setter
public class FakeOauthResolveUseCase implements OauthResolveUseCase {

    private String lastTempToken;
    private OauthUserResult oauthUserResult;
    private boolean loginAvailableScenario = true;

    @Override
    public OauthUserResult findOrRequireSignup(String tempToken) {
        this.lastTempToken = tempToken;

        if (oauthUserResult != null) {
            return oauthUserResult;
        }

        if (loginAvailableScenario) {
            AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                    .userGuid(TEST_USER_GUID_1)
                    .email(TEST_EMAIL_1)
                    .userRole(UserRole.USER)
                    .build();
            return OauthUserResult.success(authenticatedUser);
        } else {
            return OauthUserResult.requiresSignup(tempToken);
        }
    }

    @Override
    public OauthUser extractOauthUser(SignupOauthUserCommand signupOauthUserCommand) {
        return new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
    }

    public String getLastTempToken() {
        return lastTempToken;
    }
}