package teamdevhub.devhub.fake.pure.usecase.user;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

public class FakeUserSignupUseCase implements UserSignupUseCase {

    private final Map<String, User> store = new HashMap<>();

    @Override
    public User signup(SignupCommand signupCommand) {
        User user = User.createGeneralUser(
                TEST_USER_GUID_1,
                signupCommand.getEmail(),
                signupCommand.getPassword(),
                signupCommand.getUsername(),
                signupCommand.getIntroduction()
        );
        store.put(user.getUserGuid(), user);
        return user;
    }
}
