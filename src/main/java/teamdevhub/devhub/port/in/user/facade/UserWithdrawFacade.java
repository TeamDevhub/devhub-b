package teamdevhub.devhub.port.in.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.in.user.usecase.UserWithdrawUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWithdrawFacade {

    private final UserWithdrawUseCase userWithdrawUseCase;
    private final AuthenticationUseCase authenticationUseCase;

    public void withdraw(String userGuid) {
        userWithdrawUseCase.withdraw(userGuid);
        authenticationUseCase.revoke(userGuid);
    }
}
