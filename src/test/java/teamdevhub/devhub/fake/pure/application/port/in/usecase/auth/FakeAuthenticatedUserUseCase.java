package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth;

import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.user.CreateUserCommand;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticatedUserUseCase;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeAuthenticatedUserUseCase implements AuthenticatedUserUseCase {

    private final Map<String, User> store = new HashMap<>();

    public FakeAuthenticatedUserUseCase() {
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        store.put(TEST_USER_GUID_1, testUser);
    }

    @Override
    public AuthenticatedUser getUserForReissue(String userGuid) {
        return store.values().stream()
                .filter(user -> user.getUserGuid().equals(userGuid))
                .findFirst()
                .map(user -> new AuthenticatedUser(user.getUserGuid(), user.getEmail(), user.getPassword(), user.getUserRole()))
                .orElse(null);
    }

    @Override
    public AuthenticatedUser authenticate(LoginCommand loginCommand) {
        return store.values().stream()
                .filter(user -> user.getEmail().equals(loginCommand.email()))
                .findFirst()
                .map(user -> new AuthenticatedUser(user.getUserGuid(), user.getEmail(), user.getPassword(), user.getUserRole()))
                .orElse(null);
    }
}
