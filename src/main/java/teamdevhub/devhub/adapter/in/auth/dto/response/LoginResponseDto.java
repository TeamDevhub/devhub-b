package teamdevhub.devhub.adapter.in.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.common.enums.TokenPrefix;

@Getter
@Builder
public class LoginResponseDto {

    private String accessToken;
    private String refreshToken;

    public String toAuthorizationHeader() {
        return TokenPrefix.BEARER.withToken(accessToken);
    }

    public static LoginResponseDto of(String accessToken, String refreshToken){
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
