package teamdevhub.devhub.core.user.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.command.UpdatePasswordCommand;
import teamdevhub.devhub.core.user.port.in.command.UpdateProfileCommand;
import teamdevhub.devhub.core.user.port.in.command.UpdateProfileImageCommand;
import teamdevhub.devhub.core.user.port.in.facade.model.UserBasicResponseDto;
import teamdevhub.devhub.core.user.port.in.facade.model.UserDetailResponseDto;
import teamdevhub.devhub.core.user.port.in.usecase.UserProfileUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileFacade {

    private final UserProfileUseCase userProfileUseCase;
    private final UserCredentialUseCase userCredentialUseCase;

    public UserBasicResponseDto getUserInfo(String userGuid) {
        User user = userProfileUseCase.getUserInfo(userGuid);
        return UserBasicResponseDto.fromDomain(user);
    }

    public UserDetailResponseDto getCurrentUserProfile(String userGuid) {
        User user = userProfileUseCase.getCurrentUserProfile(userGuid);
        return UserDetailResponseDto.fromDomain(user);
    }

    public void updateProfileImage(UpdateProfileImageCommand updateProfileImageCommand) {
        userProfileUseCase.updateProfileImage(updateProfileImageCommand);
    }

    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        userProfileUseCase.updateProfile(updateProfileCommand);
    }

    public void updatePassword(UpdatePasswordCommand updatePasswordCommand) {
        userCredentialUseCase.updatePassword(updatePasswordCommand);
    }
}
