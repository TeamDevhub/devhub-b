package teamdevhub.devhub.adapter.in.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {

    private String prefix;
    private String accessToken;
    private String refreshToken;

    public String toAuthorizationHeader() {
        return prefix + " " + accessToken;
    }

    public static LoginResponseDto of(String prefix, String accessToken, String refreshToken){
        return LoginResponseDto.builder()
                .prefix(prefix)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
