package teamdevhub.devhub.port.in.user.usecase;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;

public interface UserProfileUseCase {

    User getCurrentUserProfile(String userGuid);
    void updateProfile(UpdateProfileCommand updateProfileCommand);
}