package teamdevhub.devhub.core.user.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.OauthUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.usecase.UserSignupUseCase;
import teamdevhub.devhub.core.auth.port.out.password.EncodedPasswordProvider;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.user.port.out.UserPositionRepository;
import teamdevhub.devhub.core.user.port.out.UserRepository;
import teamdevhub.devhub.core.user.port.out.UserSkillRepository;

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
    public String signup(SignupUserCommand signupUserCommand) {
        User user = createGeneralUser(signupUserCommand);
        saveUserPositions(user.getUserGuid(), signupUserCommand.positionList());
        saveUserSkills(user.getUserGuid(), signupUserCommand.skillList());
        return userRepository.save(user).getUserGuid();
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
