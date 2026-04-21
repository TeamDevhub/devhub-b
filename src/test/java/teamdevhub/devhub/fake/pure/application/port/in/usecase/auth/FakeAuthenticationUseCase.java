package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth;

import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticationUseCase;

public class FakeAuthenticationUseCase implements AuthenticationUseCase {

    private String revokedUserGuid;
    private UserCredential lastLoginUser;

    @Override
    public AuthResult login(UserCredential userCredential) {
        this.lastLoginUser = userCredential;
        return AuthResult.of("access-token", "refresh-token");
    }

    @Override
    public AuthResult reissueAccessToken(UserCredential userCredential) {
        return AuthResult.ofReissue("new-access-token");
    }

    @Override
    public void revoke(String userGuid) {
        this.revokedUserGuid = userGuid;
    }

    public String getRevokedUserGuid() {
        return revokedUserGuid;
    }

    public UserCredential getLastLoginUser() {
        return lastLoginUser;
    }
}
