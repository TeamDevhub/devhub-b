package teamdevhub.devhub.adapter.in.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String tokenType;
    private String accessToken;
    private String refreshToken;

    public String toAuthorizationHeader() {
        return tokenType + " " + accessToken;
    }
}
