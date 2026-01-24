package teamdevhub.devhub.adapter.in.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.application.service.oauth.vo.OauthCallbackResult;
import teamdevhub.devhub.common.enums.TokenPrefix;

@Getter
@Builder
public class OauthAuthResponseDto {

    private String accessToken;
    private String refreshToken;
    private String tempToken;

    public static OauthAuthResponseDto fromCallback(OauthCallbackResult oauthCallbackResult) {
        return OauthAuthResponseDto.builder()
                .tempToken(oauthCallbackResult.tempToken())
                .build();
    }

    public static OauthAuthResponseDto loggedIn(LoginResponseDto loginResponseDto) {
        return OauthAuthResponseDto.builder()
                .accessToken(loginResponseDto.getAccessToken())
                .refreshToken(loginResponseDto.getRefreshToken())
                .build();
    }

    public static OauthAuthResponseDto requiresSignup(String tempToken) {
        return OauthAuthResponseDto.builder()
                .tempToken(tempToken)
                .build();
    }

    public String toAuthorizationHeader() {
        return TokenPrefix.BEARER.withToken(accessToken);
    }
}
