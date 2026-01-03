package teamdevhub.devhub.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.exception.BusinessRuleException;
import teamdevhub.devhub.domain.common.record.auth.AuthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.port.out.common.DateTimeProvider;
import teamdevhub.devhub.port.out.common.IdentifierProvider;
import teamdevhub.devhub.port.out.common.PasswordPolicyProvider;
import teamdevhub.devhub.port.out.mail.EmailCertificationRepository;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final EmailCertificationRepository emailCertificationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordPolicyProvider passwordPolicyProvider;
    private final IdentifierProvider identifierProvider;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public void initializeAdminUser(String email, String rawPassword, String username) {
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = passwordPolicyProvider.encode(rawPassword);
        User adminUser = User.createAdminUser(userGuid, email, encodedPassword, username);
        update(adminUser);
    }

    @Override
    public User signup(SignupCommand signupCommand) {
        if (!emailCertificationRepository.isVerified(signupCommand.getEmail())) {
            throw BusinessRuleException.of(ErrorCodeEnum.EMAIL_NOT_CONFIRMED);
        }
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = passwordPolicyProvider.encode(signupCommand.getPassword());
        User user = User.createGeneralUser(
                userGuid,
                signupCommand.getEmail(),
                encodedPassword,
                signupCommand.getUsername(),
                signupCommand.getIntroduction(),
                signupCommand.getPositionList(),
                signupCommand.getSkillList());
        emailCertificationRepository.delete(signupCommand.getEmail());
        return userRepository.saveNewUser(user);
    }

    @Override
    public AuthUser getAuthUser(String email) {
        return userRepository.findAuthUserByEmail(email);
    }

    @Override
    public void updateLastLoginDateTime(String userGuid) {
        userRepository.updateLastLoginDateTime(userGuid, dateTimeProvider.now());
    }

    @Override
    public User getCurrentUserProfile(String userGuid) {
        return getUserByUserGuid(userGuid);
    }

    @Override
    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        User user = getUserByUserGuid(updateProfileCommand.getUserGuid());

        user.updateProfile(
                updateProfileCommand.getUsername(),
                updateProfileCommand.getIntroduction(),
                updateProfileCommand.getPositionList(),
                updateProfileCommand.getSkillList()
        );

        update(user);
    }

    @Override
    public void withdrawCurrentUser(String userGuid) {
        User user = getUserByUserGuid(userGuid);
        user.withdraw();
        refreshTokenRepository.deleteByEmail(user.getEmail());
        update(user);
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return userRepository.existsByUserRole(userRole);
    }

    private User getUserByUserGuid(String userGuid) {
        return userRepository.findByUserGuid(userGuid);
    }

    private void update(User user) {
        userRepository.updateUser(user);
    }
}