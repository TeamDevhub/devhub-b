package teamdevhub.devhub.adapter.in.user.command;

import teamdevhub.devhub.adapter.in.user.dto.SignupRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SignupCommand {

    private final String email;
    private final String username;
    private final String password;
    private String introduction;

    public static SignupCommand fromSignupUserRequestDto(SignupRequestDto signupRequestDto) {
        return SignupCommand.builder()
                .email(signupRequestDto.getEmail())
                .username(signupRequestDto.getUsername())
                .password(signupRequestDto.getPassword())
                .introduction(signupRequestDto.getIntroduction())
                .build();
    }
}