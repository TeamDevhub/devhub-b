package teamdevhub.devhub.adapter.in.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.port.in.user.usecase.UserProfileUseCase;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;
import teamdevhub.devhub.port.in.user.usecase.UserWithdrawUseCase;

@Service
@RequiredArgsConstructor
@Transactional
public class UserFacade {

    private final UserSignupUseCase userSignupUseCase;
    private final UserProfileUseCase userProfileUseCase;
    private final UserWithdrawUseCase userWithdrawUseCase;

    public User signup(SignupCommand signupCommand) {
        return userSignupUseCase.signup(signupCommand);
    }

    public User getUserDetailProfile(String userGuid) {
        return userProfileUseCase.getCurrentUserProfile(userGuid);
    }

    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        userProfileUseCase.updateProfile(updateProfileCommand);
    }

    public void withdrawUser(String userGuid) {
        userWithdrawUseCase.withdrawUser(userGuid);
    }
}
