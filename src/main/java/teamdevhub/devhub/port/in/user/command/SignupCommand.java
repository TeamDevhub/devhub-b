package teamdevhub.devhub.port.in.user.command;

import lombok.Builder;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

import java.util.List;

@Builder
public record SignupCommand(String userGuid, String email, String password, String username, String introduction,
                            List<String> positionList, List<String> skillList, VerificationTarget verificationTarget) {

}