package teamdevhub.devhub.fake.pure.usecase.user;

import teamdevhub.devhub.core.auth.domain.vo.user.OauthUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.user.CreateUserCommand;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.usecase.UserSignupUseCase;

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
        CreateUserCommand createUserCommand = CreateUserCommand.adminUserCreateCommand(signupAdminCommand, ADMIN_USER_GUID_1, ADMIN_PASSWORD_1);
        User adminUser = User.createAdminUser(createUserCommand);
        store.put(adminUser.getUserGuid(), adminUser);
    }

    @Override
    public void signup(SignupUserCommand signupUserCommand) {
        this.called = true;
        CreateUserCommand createUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User user = User.createGeneralUser(createUserCommand);
        store.put(user.getUserGuid(), user);
    }

    @Override
    public User signupWithOauth(SignupOauthUserCommand signupOauthUserCommand, OauthUser oauthUser) {
        this.called = true;
        this.lastSignupOauthUserCommand = signupOauthUserCommand;
        this.lastOauthUser = oauthUser;
        CreateUserCommand oauthCreateUserCommand = CreateUserCommand.oauthUserCreateCommand(signupOauthUserCommand, oauthUser, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User createdOauthUser = User.createOauthUser(oauthCreateUserCommand);
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
