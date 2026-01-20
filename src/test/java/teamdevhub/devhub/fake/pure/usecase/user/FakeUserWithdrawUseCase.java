package teamdevhub.devhub.fake.pure.usecase.user;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.usecase.UserWithdrawUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserWithdrawUseCase implements UserWithdrawUseCase {

    private final Map<String, User> store = new HashMap<>();

    public FakeUserWithdrawUseCase() {
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
        UserCreateCommand generalUserCreateCommand = UserCreateCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);

        store.put(TEST_USER_GUID_1, testUser);
    }

    @Override
    public void withdrawUser(String userGuid) {
        User user = store.get(userGuid);
        if (user != null) {
            user.withdraw();
        }
    }

    public User getUser(String userGuid) {
        return store.get(userGuid);
    }
}
