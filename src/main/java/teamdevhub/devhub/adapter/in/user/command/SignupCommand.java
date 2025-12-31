package teamdevhub.devhub.adapter.in.user.command;

import teamdevhub.devhub.adapter.in.user.dto.SignupUserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignupCommand {

    private final String email;
    private final String username;
    private final String password;

    public static SignupCommand fromSignupUserRequestDto(SignupUserRequestDto signupUserRequestDto) {
        return SignupCommand.builder()
                .email(signupUserRequestDto.getEmail())
                .username(signupUserRequestDto.getUsername())
                .password(signupUserRequestDto.getPassword())
                .build();
    }
}