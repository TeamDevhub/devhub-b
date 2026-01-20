package teamdevhub.devhub.port.in.user.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AdminSignupCommand {

    private String userGuid;
    private final String email;
    private String password;
    private final String username;
    private final String introduction;
    private final List<String> positionList;
    private final List<String> skillList;
    private final VerificationTarget verificationTarget;
}
