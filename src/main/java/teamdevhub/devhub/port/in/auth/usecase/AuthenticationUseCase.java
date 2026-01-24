package teamdevhub.devhub.port.in.auth.usecase;

import teamdevhub.devhub.application.service.auth.vo.AuthResult;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;

public interface AuthenticationUseCase {

    AuthResult login(AuthenticatedUser authenticatedUser);
    AuthResult reissueAccessToken(AuthenticatedUser authenticatedUser);
    void revoke(String userGuid);
}
