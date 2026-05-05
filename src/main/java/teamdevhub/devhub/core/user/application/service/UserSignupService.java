package teamdevhub.devhub.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.core.auth.port.out.password.EncodedPasswordProvider;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.usecase.UserSignupUseCase;
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
    public void saveEmailUserInfo(SignupUserCommand signupUserCommand, String userGuid) {
        User user = createGeneralUser(signupUserCommand, userGuid);
        saveUserPositions(userGuid, signupUserCommand.positionList());
        saveUserSkills(userGuid, signupUserCommand.skillList());
        userRepository.save(user);
    }

    @Override
    public void saveOAuthUserInfo(SignupOAuthUserCommand signupOAuthUserCommand, String userGuid) {
        User user = createOAuthUser(signupOAuthUserCommand, userGuid);
        saveUserPositions(user.getUserGuid(), signupOAuthUserCommand.positionList());
        saveUserSkills(user.getUserGuid(), signupOAuthUserCommand.skillList());
        userRepository.save(user);
    }

    private User createGeneralUser(SignupUserCommand signupUserCommand, String userGuid) {
        CreateUserCommand generalUserCreateCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, userGuid);
        return User.createGeneralUser(generalUserCreateCommand);
    }

    private User createOAuthUser(SignupOAuthUserCommand signupOAuthUserCommand, String userGuid) {
        CreateUserCommand createOAuthUserCommand = CreateUserCommand.oauthUserCreateCommand(signupOAuthUserCommand, userGuid);
        return User.createOAuthUser(createOAuthUserCommand);
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
