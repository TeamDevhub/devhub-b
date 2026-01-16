package teamdevhub.devhub.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.authentication.AuthenticationUseCase;
import teamdevhub.devhub.port.in.user.UserWithdrawUseCase;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWithdrawService implements UserWithdrawUseCase {

    private final AuthenticationUseCase authenticationUseCase;
    private final UserRepository userRepository;

    @Override
    public void withdrawUser(String userGuid) {
        User user = getUser(userGuid);
        user.withdraw();
        authenticationUseCase.revoke(userGuid);
        userRepository.delete(user);
    }

    private User getUser(String userGuid) {
        return userRepository.findByUserGuid(userGuid);
    }

}
