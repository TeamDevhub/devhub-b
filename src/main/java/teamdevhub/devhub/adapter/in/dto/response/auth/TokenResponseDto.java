package teamdevhub.devhub.adapter.in.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDto {

    private String accessToken;

    public static TokenResponseDto issue(String accessToken) {
        return new TokenResponseDto(accessToken);
    }
}