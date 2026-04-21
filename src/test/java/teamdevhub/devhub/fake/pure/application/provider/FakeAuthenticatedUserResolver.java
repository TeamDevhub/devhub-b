package teamdevhub.devhub.fake.pure.application.provider;

import teamdevhub.devhub.core.auth.port.out.AuthenticatedUserResolver;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.auth.domain.UserCredential;

import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

public class FakeAuthenticatedUserResolver implements AuthenticatedUserResolver {

    @Override
    public UserCredential getAuthenticatedUser(String email, String password) {
        return new UserCredential(
                TEST_USER_GUID_1,
                email,
                UserRole.USER
        );
    }
}
