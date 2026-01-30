package teamdevhub.devhub.core.user.port.in.command;

import lombok.Builder;
import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;

import java.util.List;

@Builder
public record SignupUserCommand(String email, String password, String username, String introduction,
                                List<String> positionList, List<String> skillList, VerificationTarget verificationTarget) {

}