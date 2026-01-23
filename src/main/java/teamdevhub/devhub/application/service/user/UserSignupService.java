package teamdevhub.devhub.application.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.position.UserPosition;
import teamdevhub.devhub.domain.user.vo.skill.UserSkill;
import teamdevhub.devhub.domain.user.vo.user.CreateUserCommand;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.user.command.SignupAdminCommand;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;
import teamdevhub.devhub.port.out.provider.EncodedPasswordProvider;
import teamdevhub.devhub.port.out.provider.IdentifierProvider;
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
    private final UserRepository userRepository;
    private final UserPositionRepository userPositionRepository;
    private final UserSkillRepository userSkillRepository;

    @Override
    public void initializeAdminUser(SignupAdminCommand signupAdminCommand) {
        if(existsByUserRole()) {
            return;
        }

        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = encodedPasswordProvider.encode(signupAdminCommand.password());

        CreateUserCommand adminCreateUserCommand = CreateUserCommand.adminUserCreateCommand(signupAdminCommand, userGuid, encodedPassword);
        User adminUser = User.createAdminUser(adminCreateUserCommand);
        userRepository.saveAdminUser(adminUser);
    }

    @Override
    public User signup(SignupUserCommand signupUserCommand) {
        User user = createGeneralUser(signupUserCommand);
        saveUserPositions(user.getUserGuid(), signupUserCommand.positionList());
        saveUserSkills(user.getUserGuid(), signupUserCommand.skillList());
        return userRepository.save(user);
    }

    @Override
    public User signupWithOauth(SignupOauthUserCommand signupOauthUserCommand, OauthUser oauthUser) {
        User user = createOauthUserForSignup(signupOauthUserCommand, oauthUser);
        saveUserPositions(user.getUserGuid(), signupOauthUserCommand.positionList());
        saveUserSkills(user.getUserGuid(), signupOauthUserCommand.skillList());
        return userRepository.save(user);
    }

    private User createGeneralUser(SignupUserCommand signupUserCommand) {
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = encodedPasswordProvider.encode(signupUserCommand.password());

        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, userGuid, encodedPassword);
        return User.createGeneralUser(generalCreateUserCommand);
    }

    private User createOauthUserForSignup(SignupOauthUserCommand signupOauthUserCommand, OauthUser oauthUser) {
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = encodedPasswordProvider.encode(signupOauthUserCommand.password());

        CreateUserCommand oauthCreateUserCommand = CreateUserCommand.oauthUserCreateCommand(signupOauthUserCommand, oauthUser, userGuid, encodedPassword);
        return User.createOauthUser(oauthCreateUserCommand);
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
