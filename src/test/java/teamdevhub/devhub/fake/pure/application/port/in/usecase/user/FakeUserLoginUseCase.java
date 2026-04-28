package teamdevhub.devhub.fake.pure.application.port.in.usecase.user;

import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.usecase.UserLoginUseCase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserLoginUseCase implements UserLoginUseCase {

    private final Map<String, User> store = new HashMap<>();
    private final Set<String> updatedLoginUsers = new HashSet<>();

    public FakeUserLoginUseCase() {
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        store.put(TEST_USER_GUID_1, testUser);
    }

    @Override
    public void updateLastLoginDateTime(String userGuid) {
        updatedLoginUsers.add(userGuid);
    }

    @Override
    public void validateLoginUser(String userGuid) {
        User user = store.get(userGuid);
        if (user != null) {
            user.assertActive();
        }
    }

    public boolean isLoginTimeUpdated(String userGuid) {
        return updatedLoginUsers.contains(userGuid);
    }

    public void givenUser(User user) {
        store.put(user.getUserGuid(), user);
    }

}

