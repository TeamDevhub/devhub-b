package teamdevhub.devhub.port.in.user.command;

import teamdevhub.devhub.adapter.in.dto.request.user.SignupRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SignupCommand {

    private final String email;

    private final String password;

    private final String username;

    private String introduction;

    private List<String> positionList;

    private List<String> skillList;

    private final VerificationTarget verificationTarget;

    public static SignupCommand fromSignupUserRequestDto(SignupRequestDto signupRequestDto) {
        return SignupCommand.builder()
                .email(signupRequestDto.getEmail())
                .password(signupRequestDto.getPassword())
                .username(signupRequestDto.getUsername())
                .introduction(signupRequestDto.getIntroduction())
                .positionList(signupRequestDto.getPositionList())
                .skillList(signupRequestDto.getSkillList())
                .verificationTarget(VerificationTarget.of(VerificationType.EMAIL, signupRequestDto.getEmail()))
                .build();
    }
}