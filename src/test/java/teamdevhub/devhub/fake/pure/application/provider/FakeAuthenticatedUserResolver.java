package teamdevhub.devhub.fake.pure.application.provider;

import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.auth.port.out.AuthenticatedUserResolver;

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
