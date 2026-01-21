package teamdevhub.devhub.fake.pure.usecase.user;

import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;
import teamdevhub.devhub.port.in.user.command.AdminSignupCommand;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserSignupUseCase implements UserSignupUseCase {

    private final Map<String, User> store = new HashMap<>();
    private OauthSignupCommand lastOauthSignupCommand;
    private OauthUser lastOauthUser;
    boolean called = false;

    @Override
    public void initializeAdminUser(AdminSignupCommand adminSignupCommand) {
        UserCreateCommand userCreateCommand = UserCreateCommand.adminUserCreateCommand(adminSignupCommand, ADMIN_USER_GUID_1, ADMIN_PASSWORD_1);
        User adminUser = User.createAdminUser(userCreateCommand);
        store.put(adminUser.getUserGuid(), adminUser);
    }

    @Override
    public User signup(SignupCommand signupCommand) {
        UserCreateCommand userCreateCommand = UserCreateCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User user = User.createGeneralUser(userCreateCommand);
        store.put(user.getUserGuid(), user);
        return user;
    }

    @Override
    public void signupWithOauth(OauthSignupCommand oauthSignupCommand, OauthUser oauthUser) {
        this.called = true;
        this.lastOauthSignupCommand = oauthSignupCommand;
        this.lastOauthUser = oauthUser;
    }

    public boolean isSignupCalled() {
        return called;
    }

    public OauthSignupCommand getLastOauthSignupCommand() {
        return lastOauthSignupCommand;
    }

    public OauthUser getLastOauthUser() {
        return lastOauthUser;
    }
}
