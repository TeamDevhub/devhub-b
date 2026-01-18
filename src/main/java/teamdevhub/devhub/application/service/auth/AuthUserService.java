package teamdevhub.devhub.application.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.in.auth.AuthUserUseCase;
import teamdevhub.devhub.port.out.provider.IdentifierProvider;
import teamdevhub.devhub.port.out.provider.PasswordPolicyProvider;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthUserService implements AuthUserUseCase {

    private final PasswordPolicyProvider passwordPolicyProvider;
    private final IdentifierProvider identifierProvider;
    private final UserRepository userRepository;

    @Override
    public void initializeAdminUser(String email, String rawPassword, String username) {
        if(existsByUserRole()) {
            return;
        }

        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = passwordPolicyProvider.encode(rawPassword);
        User adminUser = User.createAdminUser(userGuid, email, encodedPassword, username);
        userRepository.saveAdminUser(adminUser);
    }

    @Override
    public AuthenticatedUser getUserForLogin(String email) {
        return userRepository.findAuthenticatedUserByEmail(email);
    }

    @Override
    public AuthenticatedUser getUserForReissue(String userGuid) {
        return userRepository.findAuthenticatedUserByUserGuid(userGuid);
    }

    private boolean existsByUserRole() {
        return userRepository.existsByUserRole(UserRole.ADMIN);
    }
}
