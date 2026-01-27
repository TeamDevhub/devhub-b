package teamdevhub.devhub.core.user.port.in.usecase;

import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.command.UpdateProfileCommand;

public interface UserProfileUseCase {

    User getCurrentUserProfile(String userGuid);
    void updateProfile(UpdateProfileCommand updateProfileCommand);
}