package teamdevhub.devhub.port.in.auth.usecase;

import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;

public interface AuthenticatedUserUseCase {

    AuthenticatedUser getUserForReissue(String userGuid);
    AuthenticatedUser authenticate(LoginCommand loginCommand);
}
