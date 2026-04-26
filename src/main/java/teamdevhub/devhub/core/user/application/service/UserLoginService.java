package teamdevhub.devhub.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.usecase.UserLoginUseCase;
import teamdevhub.devhub.core.common.provider.TimeProvider;
import teamdevhub.devhub.core.user.port.out.UserRepository;

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

    @Override
    public void validateLoginUser(String userGuid) {
        User user = userRepository.findByUserGuid(userGuid);
        user.assertActive();
    }
}
