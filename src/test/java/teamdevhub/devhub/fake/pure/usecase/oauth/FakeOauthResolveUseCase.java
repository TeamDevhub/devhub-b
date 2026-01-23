package teamdevhub.devhub.fake.pure.usecase.oauth;

import lombok.Setter;
import teamdevhub.devhub.application.service.oauth.vo.OauthUserResult;
import teamdevhub.devhub.common.enums.SignupStatus;
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

    private ResolveOauthUserCommand lastCommand;
    private OauthUserResult oauthUserResult;
    private boolean loginAvailableScenario = true;

    @Override
    public OauthUserResult resolveOauthUser(ResolveOauthUserCommand resolveOauthUserCommand) {
        this.lastCommand = resolveOauthUserCommand;

        if (oauthUserResult != null) {
            return oauthUserResult;
        }

        if (loginAvailableScenario) {
            AuthenticatedUser fakeUser = AuthenticatedUser.builder()
                    .userGuid(TEST_USER_GUID_1)
                    .email(TEST_EMAIL_1)
                    .userRole(UserRole.USER)
                    .signupStatus(SignupStatus.COMPLETED)
                    .build();
            return OauthUserResult.success(fakeUser);
        } else {
            return OauthUserResult.requiresSignup(resolveOauthUserCommand.tempToken());
        }
    }

    @Override
    public OauthUser extractOauthUser(SignupOauthUserCommand signupOauthUserCommand) {
        return new OauthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
    }

    public ResolveOauthUserCommand getLastCommand() {
        return lastCommand;
    }
}