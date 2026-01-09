package teamdevhub.devhub.port.in.auth.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.adapter.in.auth.dto.request.LoginRequestDto;

@Getter
@Builder
@AllArgsConstructor
public class LoginCommand {

    private final String email;

    private final String password;

    public static LoginCommand fromLoginRequestDto(LoginRequestDto loginRequestDto) {
        return LoginCommand.builder()
                .email(loginRequestDto.getEmail())
                .password(loginRequestDto.getPassword())
                .build();
    }
}
