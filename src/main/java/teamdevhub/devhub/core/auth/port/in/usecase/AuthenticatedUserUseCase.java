package teamdevhub.devhub.core.auth.port.in.usecase;

import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;

public interface AuthenticatedUserUseCase {

    AuthenticatedUser getUserForReissue(String userGuid);
    AuthenticatedUser authenticate(LoginCommand loginCommand);
}
