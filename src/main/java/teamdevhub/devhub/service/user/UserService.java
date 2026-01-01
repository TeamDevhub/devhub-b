package teamdevhub.devhub.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.exception.BusinessRuleException;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenPort;
import teamdevhub.devhub.port.out.common.DateTimeProvider;
import teamdevhub.devhub.port.out.common.IdentifierProvider;
import teamdevhub.devhub.port.out.common.PasswordPolicyProvider;
import teamdevhub.devhub.port.out.mail.EmailCertificationPort;
import teamdevhub.devhub.port.out.user.UserPort;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserUseCase {

    private final UserPort userPort;
    private final EmailCertificationPort emailCertificationPort;
    private final RefreshTokenPort refreshTokenPort;
    private final PasswordPolicyProvider passwordPolicyProvider;
    private final IdentifierProvider identifierProvider;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public User signup(SignupCommand signupCommand) {
        if (!emailCertificationPort.isVerified(signupCommand.getEmail())) {
            throw BusinessRuleException.of(ErrorCodeEnum.EMAIL_NOT_CONFIRMED);
        }
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = passwordPolicyProvider.encode(signupCommand.getPassword());
        User user = User.createGeneralUser(userGuid, signupCommand.getEmail(), signupCommand.getUsername(), encodedPassword, signupCommand.getIntroduction());
        emailCertificationPort.delete(signupCommand.getEmail());
        return userPort.save(user);
    }

    @Override
    public void initializeAdminUser(String email, String username, String rawPassword) {
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = passwordPolicyProvider.encode(rawPassword);
        User adminUser = User.createAdminUser(userGuid, email, username, encodedPassword);
        userPort.save(adminUser);
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return userPort.existsByUserRole(userRole);
    }

    @Override
    public void updateLastLoginDate(String userGuid) {
        User user = userPort.findByUserGuid(userGuid).orElseThrow();
        user.login(dateTimeProvider.now());
        userPort.save(user);
    }

    @Override
    public void withdrawCurrentUser(String userGuid) {
        User user = userPort.findByUserGuid(userGuid)
                .orElseThrow(() -> BusinessRuleException.of(ErrorCodeEnum.USER_NOT_FOUND));

        user.withdraw();
        refreshTokenPort.deleteByEmail(user.getEmail());
        userPort.save(user);
    }
}