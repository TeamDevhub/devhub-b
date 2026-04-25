package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth;

import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserCredentialUseCase implements UserCredentialUseCase {

    @Override
    public String signupEmailUser(SignupUserCommand signupUserCommand) {
        return TEMP_TOKEN;
    }

    @Override
    public UserCredential signupOAuthUser(OauthUser oauthUser) {
        return UserCredential.builder()
                .userGuid(TEST_USER_GUID_1)
                .build();
    }

    @Override
    public UserCredential getUserForReissue(String refreshToken) {
        return UserCredential.builder()
                .userGuid(TEST_USER_GUID_1)
                .build();
    }

    @Override
    public UserCredential authenticate(LoginCommand loginCommand) {
        return UserCredential.builder()
                .userGuid(TEST_USER_GUID_1)
                .loginId(loginCommand.email())
                .build();
    }
}
