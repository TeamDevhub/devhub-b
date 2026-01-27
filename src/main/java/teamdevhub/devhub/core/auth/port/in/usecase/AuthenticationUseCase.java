package teamdevhub.devhub.core.auth.port.in.usecase;

import teamdevhub.devhub.core.auth.application.service.vo.AuthResult;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;

public interface AuthenticationUseCase {

    AuthResult login(AuthenticatedUser authenticatedUser);
    AuthResult reissueAccessToken(AuthenticatedUser authenticatedUser);
    void revoke(String userGuid);
}
