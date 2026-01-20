package teamdevhub.devhub.port.in.auth.usecase;

import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;

public interface AuthenticatedUserUseCase {

    AuthenticatedUser getUserForLogin(String email);
    AuthenticatedUser getUserForReissue(String userGuid);
}
