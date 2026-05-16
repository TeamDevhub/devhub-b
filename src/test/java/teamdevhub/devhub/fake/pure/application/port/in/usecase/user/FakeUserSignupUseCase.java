package teamdevhub.devhub.fake.pure.application.port.in.usecase.user;

import teamdevhub.devhub.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.usecase.UserSignupUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserSignupUseCase implements UserSignupUseCase {

    private final Map<String, User> store = new HashMap<>();

    private SignupOAuthUserCommand lastSignupOAuthUserCommand;
    private OAuthUser lastOAuthUser;
    private boolean called = false;

    @Override
    public void initializeAdminUser(SignupAdminCommand signupAdminCommand) {
        CreateUserCommand createUserCommand = CreateUserCommand.adminUserCreateCommand(
                        signupAdminCommand,
                        ADMIN_USER_GUID_1,
                        ADMIN_PASSWORD_1
                );

        User adminUser = User.createAdminUser(createUserCommand);
        store.put(adminUser.getUserGuid(), adminUser);
    }

    @Override
    public void saveEmailUserInfo(SignupUserCommand signupUserCommand, String userGuid) {
        this.called = true;

        CreateUserCommand createUserCommand =
                CreateUserCommand.generalUserCreateCommand(
                        signupUserCommand,
                        userGuid
                );

        User user = User.createGeneralUser(createUserCommand);
        store.put(user.getUserGuid(), user);
    }

    @Override
    public void saveOAuthUserInfo(
            SignupOAuthUserCommand signupOAuthUserCommand,
            String userGuid
    ) {
        this.called = true;
        this.lastSignupOAuthUserCommand = signupOAuthUserCommand;

        CreateUserCommand createUserCommand =
                CreateUserCommand.oauthUserCreateCommand(
                        signupOAuthUserCommand,
                        userGuid
                );

        User user = User.createOauthUser(createUserCommand);
        store.put(user.getUserGuid(), user);
    }

    public boolean isSignupCalled() {
        return called;
    }

    public SignupOAuthUserCommand getLastOAuthSignupCommand() {
        return lastSignupOAuthUserCommand;
    }

    public OAuthUser getLastOAuthUser() {
        return lastOAuthUser;
    }

    public User getUser(String userGuid) {
        return store.get(userGuid);
    }
}