package teamdevhub.devhub.application.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.port.in.user.usecase.UserLoginUseCase;
import teamdevhub.devhub.port.out.provider.TimeProvider;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLoginService implements UserLoginUseCase {

    private final TimeProvider timeProvider;
    private final UserRepository userRepository;

    @Override
    public void updateLastLoginDateTime(String userGuid) {
        userRepository.updateLastLoginDateTime(userGuid, timeProvider.now());
    }
}
