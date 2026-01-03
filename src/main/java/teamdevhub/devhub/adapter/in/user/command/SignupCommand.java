package teamdevhub.devhub.adapter.in.user.command;

import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

    public static SignupCommand fromSignupUserRequestDto(SignupRequestDto signupRequestDto) {
        return SignupCommand.builder()
                .email(signupRequestDto.getEmail())
                .password(signupRequestDto.getPassword())
                .username(signupRequestDto.getUsername())
                .introduction(signupRequestDto.getIntroduction())
                .positionList(signupRequestDto.getPositionList())
                .skillList(signupRequestDto.getSkillList())
                .build();
    }
}