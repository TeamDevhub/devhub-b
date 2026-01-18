package teamdevhub.devhub.application.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.auth.AuthSessionUseCase;
import teamdevhub.devhub.port.in.user.usecase.UserWithdrawUseCase;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWithdrawService implements UserWithdrawUseCase {

    private final AuthSessionUseCase authSessionUseCase;
    private final UserRepository userRepository;

    @Override
    public void withdrawUser(String userGuid) {
        User user = getUser(userGuid);
        user.withdraw();
        authSessionUseCase.revoke(userGuid);
        userRepository.delete(user);
    }

    private User getUser(String userGuid) {
        return userRepository.findByUserGuid(userGuid);
    }

}
