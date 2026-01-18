package teamdevhub.devhub.adapter.in.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {

    private String accessToken;

    public static TokenResponseDto issue(String accessToken) {
        return new TokenResponseDto(accessToken);
    }
}