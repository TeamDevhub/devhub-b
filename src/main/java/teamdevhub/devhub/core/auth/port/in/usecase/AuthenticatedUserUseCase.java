package teamdevhub.devhub.core.auth.port.in.usecase;

import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;

public interface AuthenticatedUserUseCase {

    UserCredential getUserForReissue(String userGuid);
    UserCredential authenticate(LoginCommand loginCommand);
}
