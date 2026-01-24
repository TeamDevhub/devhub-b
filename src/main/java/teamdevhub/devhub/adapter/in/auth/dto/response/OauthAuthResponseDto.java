package teamdevhub.devhub.adapter.in.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.TokenPrefix;

@Getter
@Builder
public class OauthAuthResponseDto {

    private SignupStatus signupStatus;
    private String accessToken;
    private String refreshToken;
    private String tempToken;

    public static OauthAuthResponseDto loggedIn(LoginResponseDto loginResponseDto) {
        return OauthAuthResponseDto.builder()
                .signupStatus(SignupStatus.COMPLETED)
                .accessToken(loginResponseDto.getAccessToken())
                .refreshToken(loginResponseDto.getRefreshToken())
                .build();
    }

    public static OauthAuthResponseDto requiresSignup(String tempToken) {
        return OauthAuthResponseDto.builder()
                .signupStatus(SignupStatus.PENDING)
                .tempToken(tempToken)
                .build();
    }

    public String toAuthorizationHeader() {
        return TokenPrefix.BEARER.withToken(accessToken);
    }
}
