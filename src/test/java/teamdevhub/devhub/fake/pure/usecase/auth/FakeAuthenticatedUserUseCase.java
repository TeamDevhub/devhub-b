package teamdevhub.devhub.fake.pure.usecase.auth;

import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.CreateUserCommand;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticatedUserUseCase;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeAuthenticatedUserUseCase implements AuthenticatedUserUseCase {

    private final Map<String, User> store = new HashMap<>();

    public FakeAuthenticatedUserUseCase() {
        SignupCommand signupCommand = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalUserCreateCommand = CreateUserCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);

        store.put(TEST_USER_GUID_1, testUser);
    }

    @Override
    public AuthenticatedUser getUserForLogin(String email) {
        return store.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .map(u -> new AuthenticatedUser(u.getUserGuid(), u.getEmail(), u.getPassword(), u.getUserRole()))
                .orElse(null);
    }

    @Override
    public AuthenticatedUser getUserForReissue(String userGuid) {
        return store.values().stream()
                .filter(u -> u.getUserGuid().equals(userGuid))
                .findFirst()
                .map(u -> new AuthenticatedUser(u.getUserGuid(), u.getEmail(), u.getPassword(), u.getUserRole()))
                .orElse(null);
    }
}
