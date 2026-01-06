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
    private String tokenType;
    private String accessToken;
    private String refreshToken;

    public String toAuthorizationHeader() {
        return tokenType + " " + accessToken;
    }

    public static LoginResponseDto of(String tokenType, String accessToken, String refreshToken){
        return LoginResponseDto.builder()
                .tokenType(tokenType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
