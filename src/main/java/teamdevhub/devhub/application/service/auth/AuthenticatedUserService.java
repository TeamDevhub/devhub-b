package teamdevhub.devhub.application.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticatedUserUseCase;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticatedUserService implements AuthenticatedUserUseCase {

    private final UserRepository userRepository;

    @Override
    public AuthenticatedUser getUserForLogin(String email) {
        return userRepository.findAuthenticatedUserByEmail(email);
    }

    @Override
    public AuthenticatedUser getUserForReissue(String userGuid) {
        return userRepository.findAuthenticatedUserByUserGuid(userGuid);
    }
}
