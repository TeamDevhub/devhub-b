package teamdevhub.devhub.fake.pure.usecase.user;

import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.user.command.SignupAdminCommand;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserSignupUseCase implements UserSignupUseCase {

    private final Map<String, User> store = new HashMap<>();
    private SignupOauthUserCommand lastSignupOauthUserCommand;
    private OauthUser lastOauthUser;
    boolean called = false;

    @Override
    public void initializeAdminUser(SignupAdminCommand signupAdminCommand) {
        UserCreateCommand userCreateCommand = UserCreateCommand.adminUserCreateCommand(signupAdminCommand, ADMIN_USER_GUID_1, ADMIN_PASSWORD_1);
        User adminUser = User.createAdminUser(userCreateCommand);
        store.put(adminUser.getUserGuid(), adminUser);
    }

    @Override
    public User signup(SignupUserCommand signupUserCommand) {
        UserCreateCommand userCreateCommand = UserCreateCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User user = User.createGeneralUser(userCreateCommand);
        store.put(user.getUserGuid(), user);
        return user;
    }

    @Override
    public User signupWithOauth(SignupOauthUserCommand signupOauthUserCommand, OauthUser oauthUser) {
        this.called = true;
        this.lastSignupOauthUserCommand = signupOauthUserCommand;
        this.lastOauthUser = oauthUser;
        UserCreateCommand oauthUserCreateCommand = UserCreateCommand.oauthUserCreateCommand(signupOauthUserCommand, oauthUser, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User createdOauthUser = User.createOauthUser(oauthUserCreateCommand);
        store.put(createdOauthUser.getUserGuid(), createdOauthUser);
        return createdOauthUser;
    }

    public boolean isSignupCalled() {
        return called;
    }

    public SignupOauthUserCommand getLastOauthSignupCommand() {
        return lastSignupOauthUserCommand;
    }

    public OauthUser getLastOauthUser() {
        return lastOauthUser;
    }
}
