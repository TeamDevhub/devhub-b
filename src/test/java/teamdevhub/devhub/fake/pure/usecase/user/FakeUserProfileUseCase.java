package teamdevhub.devhub.fake.pure.usecase.user;

import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.user.CreateUserCommand;
import teamdevhub.devhub.core.user.domain.vo.user.UpdateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.command.UpdateProfileCommand;
import teamdevhub.devhub.core.user.port.in.usecase.UserProfileUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserProfileUseCase implements UserProfileUseCase {

    private final Map<String, User> store = new HashMap<>();

    public FakeUserProfileUseCase() {
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
    public User getCurrentUserProfile(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        User user = store.get(updateProfileCommand.userGuid());
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(updateProfileCommand.username(), updateProfileCommand.introduction());
        user.updateBasicProfile(updateUserCommand);
        user.changePositions(updateProfileCommand.positions());
        user.changeSkills(updateProfileCommand.skills());
    }
}
