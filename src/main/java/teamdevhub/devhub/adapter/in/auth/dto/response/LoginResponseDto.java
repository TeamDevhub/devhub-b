package teamdevhub.devhub.adapter.in.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
