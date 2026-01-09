package teamdevhub.devhub.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.port.in.mail.EmailVerificationUseCase;
import teamdevhub.devhub.port.out.mail.EmailVerificationRepository;
import teamdevhub.devhub.service.common.exception.BusinessRuleException;
import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.record.UserPosition;
import teamdevhub.devhub.domain.user.record.UserSkill;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.common.provider.password.PasswordPolicyProvider;
import teamdevhub.devhub.port.out.user.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final EmailVerificationUseCase emailVerificationUseCase;
    private final EmailVerificationRepository emailVerificationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordPolicyProvider passwordPolicyProvider;
    private final IdentifierProvider identifierProvider;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public void initializeAdminUser(String email, String rawPassword, String username) {
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = passwordPolicyProvider.encode(rawPassword);
        User adminUser = User.createAdminUser(userGuid, email, encodedPassword, username);
        userRepository.saveAdminUser(adminUser);
    }

    @Override
    public AuthenticatedUser getUserForLogin(String email) {
        return userRepository.findUserByEmailForLogin(email);
    }

    @Override
    public User signup(SignupCommand signupCommand) {
        if (!emailVerificationUseCase.isVerified(signupCommand.getEmail())) {
            throw BusinessRuleException.of(ErrorCode.EMAIL_NOT_CONFIRMED);
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
        emailVerificationRepository.delete(signupCommand.getEmail());
        return userRepository.saveNewUser(user);
    }

    @Override
    public void updateLastLoginDateTime(String userGuid) {
        User user = getUserByUserGuid(userGuid);
        user.updateLastLoginDateTime(dateTimeProvider.now());
        userRepository.updateLastLoginDateTime(user);
    }

    @Override
    public User getCurrentUserProfile(String userGuid) {
        return getUserByUserGuid(userGuid);
    }

    @Override
    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        User user = getUserByUserGuid(updateProfileCommand.getUserGuid());

        Set<UserPosition> positions = updateProfileCommand.getPositionList().stream()
                .map(UserPosition::new)
                .collect(Collectors.toUnmodifiableSet());

        Set<UserSkill> skills = updateProfileCommand.getSkillList().stream()
                .map(UserSkill::new)
                .collect(Collectors.toUnmodifiableSet());

        user.updateProfile(
                updateProfileCommand.getUsername(),
                updateProfileCommand.getIntroduction(),
                positions,
                skills);

        userRepository.updateUserProfile(user);
    }

    @Override
    public void withdrawCurrentUser(String userGuid) {
        User user = getUserByUserGuid(userGuid);
        user.withdraw();
        refreshTokenRepository.deleteByUserGuid(userGuid);
        userRepository.updateUserForWithdrawal(user);
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return userRepository.existsByUserRole(userRole);
    }

    private User getUserByUserGuid(String userGuid) {
        return userRepository.findByUserGuid(userGuid);
    }
}