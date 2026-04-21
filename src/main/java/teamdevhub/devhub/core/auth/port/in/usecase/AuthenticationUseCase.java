package teamdevhub.devhub.core.auth.port.in.usecase;

import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.domain.UserCredential;

public interface AuthenticationUseCase {

    AuthResult login(UserCredential userCredential);
    AuthResult reissueAccessToken(UserCredential userCredential);
    void revoke(String userGuid);
}
