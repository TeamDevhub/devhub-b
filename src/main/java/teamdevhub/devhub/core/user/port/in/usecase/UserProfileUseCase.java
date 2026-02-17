package teamdevhub.devhub.core.user.port.in.usecase;

import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.command.UpdateProfileCommand;
import teamdevhub.devhub.core.user.port.in.command.UpdateProfileImageCommand;

public interface UserProfileUseCase {

    User getCurrentUserProfile(String userGuid);
    void updateProfileImage(UpdateProfileImageCommand updateProfileImageCommand);
    void updateProfile(UpdateProfileCommand updateProfileCommand);
}