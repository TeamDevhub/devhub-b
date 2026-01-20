package teamdevhub.devhub.application.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.position.UserPosition;
import teamdevhub.devhub.domain.user.vo.skill.UserSkill;
import teamdevhub.devhub.domain.user.vo.user.CreateUserCommand;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.in.user.command.AdminSignupCommand;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;
import teamdevhub.devhub.port.in.verification.VerificationUseCase;
import teamdevhub.devhub.port.out.provider.IdentifierProvider;
import teamdevhub.devhub.port.out.provider.PasswordPolicyProvider;
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

    private final PasswordPolicyProvider passwordPolicyProvider;
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
        String encodedPassword = passwordPolicyProvider.encode(adminSignupCommand.getPassword());

        CreateUserCommand adminUserCreateCommand = CreateUserCommand.adminUserCreateCommand(adminSignupCommand, userGuid, encodedPassword);
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

    private void validateSignupVerification(VerificationTarget verificationTarget) {
        verificationUseCase.assertAllowed(verificationTarget);
    }

    private User createGeneralUser(SignupCommand signupCommand) {
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = passwordPolicyProvider.encode(signupCommand.password());

        CreateUserCommand generalUserCreateCommand = CreateUserCommand.generalUserCreateCommand(signupCommand, userGuid, encodedPassword);
        return User.createGeneralUser(generalUserCreateCommand);
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
