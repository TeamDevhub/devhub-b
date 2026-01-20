package teamdevhub.devhub.fake.pure.usecase.user;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.CreateUserCommand;
import teamdevhub.devhub.domain.user.vo.user.UpdateUserCommand;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.port.in.user.usecase.UserProfileUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserProfileUseCase implements UserProfileUseCase {

    private final Map<String, User> store = new HashMap<>();

    public FakeUserProfileUseCase() {
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
        CreateUserCommand generalUserCreateCommand = CreateUserCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);

        store.put(TEST_USER_GUID_1, testUser);
    }

    @Override
    public User getCurrentUserProfile(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        User user = store.get(updateProfileCommand.getUserGuid());
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(updateProfileCommand.getUsername(), updateProfileCommand.getIntroduction());
        user.updateBasicProfile(updateUserCommand);
        user.changePositions(updateProfileCommand.getPositions());
        user.changeSkills(updateProfileCommand.getSkills());
    }
}
