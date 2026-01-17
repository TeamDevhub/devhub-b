package teamdevhub.devhub.application.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.port.in.user.usecase.UserLoginUseCase;
import teamdevhub.devhub.port.out.provider.TimeProvider;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserLoginService implements UserLoginUseCase {

    private final UserRepository userRepository;
    private final TimeProvider timeProvider;

    @Override
    public void updateLastLoginDateTime(String userGuid) {
        userRepository.updateLastLoginDateTime(userGuid, timeProvider.now());
    }
}
