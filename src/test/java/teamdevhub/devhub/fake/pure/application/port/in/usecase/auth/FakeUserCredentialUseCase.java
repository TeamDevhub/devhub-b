package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth;

import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.command.UpdatePasswordCommand;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserCredentialUseCase implements UserCredentialUseCase {

    @Override
    public String signupEmailUser(SignupUserCommand signupUserCommand) {
        return TEMP_TOKEN;
    }

    @Override
    public AuthenticatedUser signupOAuthUser(OauthUser oauthUser) {
        return AuthenticatedUser.builder()
                .userGuid(TEST_USER_GUID_1)
                .build();
    }

    @Override
    public AuthenticatedUser getUserForReissue(String refreshToken) {
        return AuthenticatedUser.builder()
                .userGuid(TEST_USER_GUID_1)
                .build();
    }

    @Override
    public AuthenticatedUser authenticate(LoginCommand loginCommand) {
        return AuthenticatedUser.builder()
                .userGuid(TEST_USER_GUID_1)
                .loginId(loginCommand.email())
                .build();
    }

    @Override
    public void updatePassword(UpdatePasswordCommand updatePasswordCommand) {

    }
}
