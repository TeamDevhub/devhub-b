package teamdevhub.devhub.fake.pure.provider;

import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.AuthenticatedUser;
import teamdevhub.devhub.port.out.auth.AuthenticatedUserProvider;

import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

public class FakeAuthenticatedUserProvider implements AuthenticatedUserProvider {

    @Override
    public AuthenticatedUser getAuthenticatedUser(String email, String password) {
        return new AuthenticatedUser(
                TEST_USER_GUID_1,
                email,
                password,
                UserRole.USER
        );
    }
}
