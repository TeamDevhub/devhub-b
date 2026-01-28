package teamdevhub.devhub.core.auth.port.in.usecase;

import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;

public interface AuthenticatedUserUseCase {

    AuthenticatedUser getUserForReissue(String userGuid);
    AuthenticatedUser authenticate(LoginCommand loginCommand);
}
