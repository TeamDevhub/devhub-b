package teamdevhub.devhub.port.in.user.usecase;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

public interface UserSignupUseCase {

    User signup(SignupCommand signupCommand);
}
