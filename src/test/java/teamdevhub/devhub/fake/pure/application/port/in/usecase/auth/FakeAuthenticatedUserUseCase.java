package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth;

import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticatedUserUseCase;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeAuthenticatedUserUseCase implements AuthenticatedUserUseCase {

    private final Map<String, UserCredential> credentialStore = new HashMap<>();
    private final Map<String, String> passwordStore = new HashMap<>();

    public FakeAuthenticatedUserUseCase() {

        UserCredential credential = UserCredential.of(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                UserRole.USER
        );

        credentialStore.put(TEST_EMAIL_1, credential);
        passwordStore.put(TEST_EMAIL_1, TEST_PASSWORD_1);
    }

    @Override
    public UserCredential getUserForReissue(String userGuid) {

        return credentialStore.values().stream()
                .filter(c -> c.userGuid().equals(userGuid))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public UserCredential authenticate(LoginCommand loginCommand) {

        UserCredential credential = credentialStore.get(loginCommand.email());

        if (credential == null) {
            throw new IllegalArgumentException("user not found");
        }

        String savedPassword = passwordStore.get(loginCommand.email());

        if (!savedPassword.equals(loginCommand.password())) {
            throw new IllegalArgumentException("invalid password");
        }

        return credential;
    }
}