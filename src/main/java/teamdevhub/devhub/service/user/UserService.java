package teamdevhub.devhub.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.*;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.in.auth.AuthenticationUseCase;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.port.in.verification.SignupVerificationUseCase;
import teamdevhub.devhub.port.out.auth.PasswordPolicyProvider;
import teamdevhub.devhub.port.out.user.UserPositionRepository;
import teamdevhub.devhub.port.out.user.UserRepository;
import teamdevhub.devhub.port.out.user.UserSkillRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserUseCase {

    private final SignupVerificationUseCase signupVerificationUseCase;
    private final AuthenticationUseCase authenticationUseCase;

    private final UserRepository userRepository;
    private final UserPositionRepository userPositionRepository;
    private final UserSkillRepository userSkillRepository;

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
        return userRepository.findAuthenticatedUserByEmail(email);
    }

    @Override
    public AuthenticatedUser getUserForReissue(String userGuid) {
        return userRepository.findAuthenticatedUserByUserGuid(userGuid);
    }

    @Override
    public User signup(SignupCommand signupCommand) {
        validateSignupVerification(signupCommand.getVerificationTarget());

        User user = createUserForSignup(signupCommand);
        saveUserPositions(user.getUserGuid(), signupCommand);
        saveUserSkills(user.getUserGuid(), signupCommand);
        User savedUser = userRepository.save(user);

        signupVerificationUseCase.consume(signupCommand.getVerificationTarget());
        return savedUser;
    }

    @Override
    public void updateLastLoginDateTime(String userGuid) {
        userRepository.updateLastLoginDateTime(userGuid, dateTimeProvider.now());
    }

    @Override
    public User getCurrentUserProfile(String userGuid) {
        return getUserWithPositionsAndSkills(userGuid);
    }

    @Override
    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        User user = getUserWithPositionsAndSkills(updateProfileCommand.getUserGuid());

        if (updateProfileCommand.hasUsernameAndIntroductionChange()) {
            user.updateUsernameAndIntroduction(updateProfileCommand.getUsername(), updateProfileCommand.getIntroduction());
            userRepository.updateUserProfile(user);
        }

        if (updateProfileCommand.hasPositionsChange()) {
            replacePositions(user, updateProfileCommand.getPositions());
        }

        if (updateProfileCommand.hasSkillsChange()) {
            replaceSkills(user, updateProfileCommand.getSkills());
        }
    }

    @Override
    public void withdrawUser(String userGuid) {
        User user = getUser(userGuid);
        user.withdraw();
        authenticationUseCase.revoke(userGuid);
        userRepository.delete(user);
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return userRepository.existsByUserRole(userRole);
    }

    private User getUser(String userGuid) {
        return userRepository.findByUserGuid(userGuid);
    }

    private User getUserWithPositionsAndSkills(String userGuid) {
        User user = userRepository.findByUserGuid(userGuid);
        Set<UserPosition> userPositions = userPositionRepository.findByUserGuid(userGuid);
        Set<UserSkill> userSkills = userSkillRepository.findByUserGuid(userGuid);
        user.loadPositionsAndSkills(userPositions, userSkills);
        return user;
    }

    private void validateSignupVerification(VerificationTarget verificationTarget) {
        signupVerificationUseCase.assertSignupAllowed(verificationTarget);
    }

    private User createUserForSignup(SignupCommand signupCommand) {
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = passwordPolicyProvider.encode(signupCommand.getPassword());
        return User.createGeneralUser(
                userGuid,
                signupCommand.getEmail(),
                encodedPassword,
                signupCommand.getUsername(),
                signupCommand.getIntroduction());
    }

    private void saveUserPositions(String userGuid, SignupCommand signupCommand) {
        Set<UserPosition> positions = signupCommand.getPositionList().stream()
                .map(position -> new UserPosition(userGuid, position))
                .collect(Collectors.toUnmodifiableSet());
        userPositionRepository.saveAll(positions);
    }

    private void saveUserSkills(String userGuid, SignupCommand signupCommand) {
        Set<UserSkill> skills = signupCommand.getSkillList().stream()
                .map(skill -> new UserSkill(userGuid, skill))
                .collect(Collectors.toUnmodifiableSet());
        userSkillRepository.saveAll(skills);
    }

    private void replacePositions(User user, Set<UserPosition> positions) {
        UserPositionChangeResult userPositionChangeResult = user.changePositions(positions);
        if (userPositionChangeResult.changed()) {
            userPositionRepository.replace(userPositionChangeResult.previousPositions(), userPositionChangeResult.currentPositions());
        }
    }

    private void replaceSkills(User user, Set<UserSkill> skills) {
        UserSkillChangeResult userSkillChangeResult = user.changeSkills(skills);
        if (userSkillChangeResult.changed()) {
            userSkillRepository.replace(userSkillChangeResult.previousSkills(), userSkillChangeResult.currentSkills());
        }
    }
}