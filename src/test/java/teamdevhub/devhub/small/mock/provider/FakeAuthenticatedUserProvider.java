package teamdevhub.devhub.small.mock.provider;

import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.port.out.auth.AuthenticatedUserProvider;

import static teamdevhub.devhub.small.mock.constant.TestConstant.TEST_GUID;

public class FakeAuthenticatedUserProvider implements AuthenticatedUserProvider {

    @Override
    public AuthenticatedUser getAuthenticatedUser(String email, String password) {
        return new AuthenticatedUser(
                TEST_GUID,
                email,
                password,
                UserRole.USER
        );
    }
}
