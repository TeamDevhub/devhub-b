package teamdevhub.devhub.application.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;
import teamdevhub.devhub.port.in.verification.VerificationUseCase;
import teamdevhub.devhub.port.out.provider.IdentifierProvider;
import teamdevhub.devhub.port.out.provider.PasswordPolicyProvider;
import teamdevhub.devhub.port.out.user.UserPositionRepository;
import teamdevhub.devhub.port.out.user.UserRepository;
import teamdevhub.devhub.port.out.user.UserSkillRepository;

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
    public User signup(SignupCommand signupCommand) {
        validateSignupVerification(signupCommand.getVerificationTarget());

        User user = createUserForSignup(signupCommand);
        saveUserPositions(user.getUserGuid(), signupCommand);
        saveUserSkills(user.getUserGuid(), signupCommand);
        User savedUser = userRepository.save(user);

        verificationUseCase.consume(signupCommand.getVerificationTarget());
        return savedUser;
    }

    private void validateSignupVerification(VerificationTarget verificationTarget) {
        verificationUseCase.assertAllowed(verificationTarget);
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
}
