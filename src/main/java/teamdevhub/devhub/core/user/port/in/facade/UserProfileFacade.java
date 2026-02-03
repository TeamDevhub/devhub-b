package teamdevhub.devhub.core.user.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.command.UpdateProfileCommand;
import teamdevhub.devhub.core.user.port.in.facade.model.UserDetailResponseDto;
import teamdevhub.devhub.core.user.port.in.usecase.UserProfileUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileFacade {

    private final UserProfileUseCase userProfileUseCase;

    public UserDetailResponseDto getCurrentUserProfile(String userGuid) {
        User user = userProfileUseCase.getCurrentUserProfile(userGuid);
        return UserDetailResponseDto.fromDomain(user);
    }

    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        userProfileUseCase.updateProfile(updateProfileCommand);
    }
}
