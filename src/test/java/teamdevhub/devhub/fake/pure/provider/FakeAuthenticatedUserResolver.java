package teamdevhub.devhub.fake.pure.provider;

import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.out.auth.AuthenticatedUserResolver;

import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

public class FakeAuthenticatedUserResolver implements AuthenticatedUserResolver {

    @Override
    public AuthenticatedUser getAuthenticatedUser(String email, String password) {
        return new AuthenticatedUser(
                TEST_USER_GUID_1,
                SignupStatus.COMPLETED,
                email,
                password,
                UserRole.USER
        );
    }
}
