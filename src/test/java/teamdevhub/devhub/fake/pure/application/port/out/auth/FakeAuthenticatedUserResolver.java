package teamdevhub.devhub.fake.pure.application.port.out.auth;

import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.out.AuthenticatedUserResolver;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

public class FakeAuthenticatedUserResolver implements AuthenticatedUserResolver {

    @Override
    public AuthenticatedUser getAuthenticatedUser(String email, String rawPassword) {
        return new AuthenticatedUser(TEST_USER_GUID_1, email, UserRole.USER);
    }
}
