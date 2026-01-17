package teamdevhub.devhub.fake.pure.usecase.user;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.port.in.user.usecase.UserProfileUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserProfileUseCase implements UserProfileUseCase {

    private final Map<String, User> store = new HashMap<>();

    public FakeUserProfileUseCase() {
        User user = User.createGeneralUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                TEST_USERNAME_1,
                TEST_INTRO_1
        );
        store.put(TEST_USER_GUID_1, user);
    }

    @Override
    public User getCurrentUserProfile(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        User user = store.get(updateProfileCommand.getUserGuid());
        user.updateUsernameAndIntroduction(updateProfileCommand.getUsername(), updateProfileCommand.getIntroduction());
        user.changePositions(updateProfileCommand.getPositions());
        user.changeSkills(updateProfileCommand.getSkills());
    }
}
