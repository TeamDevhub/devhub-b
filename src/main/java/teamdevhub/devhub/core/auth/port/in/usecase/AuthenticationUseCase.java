package teamdevhub.devhub.core.auth.port.in.usecase;

import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;

public interface AuthenticationUseCase {

    AuthResult login(AuthenticatedUser authenticatedUser);
    AuthResult reissueAccessToken(AuthenticatedUser authenticatedUser);
    void revoke(String userGuid);
}
