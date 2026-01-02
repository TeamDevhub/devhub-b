package teamdevhub.devhub.adapter.in.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDto {

    private String accessToken;

    public static TokenResponseDto of(String accessToken) {
        return new TokenResponseDto(accessToken);
    }
    public static TokenResponseDto reissue(String newAccessToken) {
        return new TokenResponseDto(newAccessToken);
    }
}