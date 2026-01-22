package teamdevhub.devhub.application.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.user.usecase.UserWithdrawUseCase;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWithdrawService implements UserWithdrawUseCase {

    private final UserRepository userRepository;

    @Override
    public void withdraw(String userGuid) {
        User user = userRepository.findByUserGuid(userGuid);
        user.withdraw();
        userRepository.delete(user);
    }
}
