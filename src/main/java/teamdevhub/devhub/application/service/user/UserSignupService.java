package teamdevhub.devhub.application.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.position.UserPosition;
import teamdevhub.devhub.domain.user.vo.skill.UserSkill;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;
import teamdevhub.devhub.port.in.user.command.AdminSignupCommand;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;
import teamdevhub.devhub.port.in.verification.VerificationUseCase;
import teamdevhub.devhub.port.out.provider.IdentifierProvider;
import teamdevhub.devhub.port.out.provider.EncodedPasswordProvider;
import teamdevhub.devhub.port.out.user.UserPositionRepository;
import teamdevhub.devhub.port.out.user.UserRepository;
import teamdevhub.devhub.port.out.user.UserSkillRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignupService implements UserSignupUseCase {

    private final EncodedPasswordProvider encodedPasswordProvider;
    private final IdentifierProvider identifierProvider;
    private final VerificationUseCase verificationUseCase;
    private final UserRepository userRepository;
    private final UserPositionRepository userPositionRepository;
    private final UserSkillRepository userSkillRepository;

    @Override
    public void initializeAdminUser(AdminSignupCommand adminSignupCommand) {
        if(existsByUserRole()) {
            return;
        }

        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = encodedPasswordProvider.encode(adminSignupCommand.password());

        UserCreateCommand adminUserCreateCommand = UserCreateCommand.adminUserCreateCommand(adminSignupCommand, userGuid, encodedPassword);
        User adminUser = User.createAdminUser(adminUserCreateCommand);
        userRepository.saveAdminUser(adminUser);
    }

    @Override
    public User signup(SignupCommand signupCommand) {
        validateSignupVerification(signupCommand.verificationTarget());

        User user = createGeneralUser(signupCommand);
        saveUserPositions(user.getUserGuid(), signupCommand.positionList());
        saveUserSkills(user.getUserGuid(), signupCommand.skillList());
        User savedUser = userRepository.save(user);

        verificationUseCase.consume(signupCommand.verificationTarget());
        return savedUser;
    }

    @Override
    public void signupWithOauth(OauthSignupCommand oauthSignupCommand, OauthUser oauthUser) {
        User user = createOauthUserForSignup(oauthSignupCommand, oauthUser);
        saveUserPositions(user.getUserGuid(), oauthSignupCommand.positionList());
        saveUserSkills(user.getUserGuid(), oauthSignupCommand.skillList());
        userRepository.save(user);
    }

    private void validateSignupVerification(VerificationTarget verificationTarget) {
        verificationUseCase.assertAllowed(verificationTarget);
    }

    private User createGeneralUser(SignupCommand signupCommand) {
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = encodedPasswordProvider.encode(signupCommand.password());

        UserCreateCommand generalUserCreateCommand = UserCreateCommand.generalUserCreateCommand(signupCommand, userGuid, encodedPassword);
        return User.createGeneralUser(generalUserCreateCommand);
    }

    private User createOauthUserForSignup(OauthSignupCommand oauthSignupCommand, OauthUser oauthUser) {
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = encodedPasswordProvider.encode(oauthSignupCommand.password());

        UserCreateCommand oauthUserCreateCommand = UserCreateCommand.oauthUserCreateCommand(oauthSignupCommand, oauthUser, userGuid, encodedPassword);
        return User.createOauthUser(oauthUserCreateCommand);
    }

    private void saveUserPositions(String userGuid, List<String> positionList) {
        Set<UserPosition> positions = positionList.stream()
                .map(position -> new UserPosition(userGuid, position))
                .collect(Collectors.toUnmodifiableSet());
        userPositionRepository.saveAll(positions);
    }

    private void saveUserSkills(String userGuid, List<String> skillList) {
        Set<UserSkill> skills = skillList.stream()
                .map(skill -> new UserSkill(userGuid, skill))
                .collect(Collectors.toUnmodifiableSet());
        userSkillRepository.saveAll(skills);
    }

    private boolean existsByUserRole() {
        return userRepository.existsByUserRole(UserRole.ADMIN);
    }

}
