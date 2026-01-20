package teamdevhub.devhub.fake.pure.usecase.user;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.CreateUserCommand;
import teamdevhub.devhub.port.in.user.command.AdminSignupCommand;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserSignupUseCase implements UserSignupUseCase {

    private final Map<String, User> store = new HashMap<>();

    @Override
    public void initializeAdminUser(AdminSignupCommand adminSignupCommand) {
        CreateUserCommand createUserCommand = CreateUserCommand.adminUserCreateCommand(adminSignupCommand, ADMIN_USER_GUID_1, ADMIN_PASSWORD_1);
        User adminUser = User.createAdminUser(createUserCommand);
        store.put(ADMIN_USER_GUID_1, adminUser);
    }

    @Override
    public User signup(SignupCommand signupCommand) {
        CreateUserCommand createUserCommand = CreateUserCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User user = User.createGeneralUser(createUserCommand);
        store.put(user.getUserGuid(), user);
        return user;
    }
}
