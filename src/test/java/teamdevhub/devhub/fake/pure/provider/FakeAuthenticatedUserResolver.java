package teamdevhub.devhub.fake.pure.provider;

import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.user.vo.UserRole;
import teamdevhub.devhub.port.out.auth.AuthenticatedUserResolver;

import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

public class FakeAuthenticatedUserResolver implements AuthenticatedUserResolver {

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
