package teamdevhub.devhub.adapter.in.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.common.enums.SignupStatus;

@Getter
@Builder
public class LoginResponseDto {

    private String prefix;
    private String accessToken;
    private String refreshToken;
    private String tempToken;
    private SignupStatus signupStatus;

    public String toAuthorizationHeader() {
        return prefix + " " + accessToken;
    }

    public static LoginResponseDto ofEmailUser(String prefix, String accessToken, String refreshToken){
        return LoginResponseDto.builder()
                .prefix(prefix)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static LoginResponseDto existedOAuthUser(String prefix, String accessToken, String refreshToken, SignupStatus signupStatus){
        return LoginResponseDto.builder()
                .prefix(prefix)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .signupStatus(signupStatus)
                .build();
    }

    public static LoginResponseDto notExistedOAuthUser(String tempToken, SignupStatus signupStatus){
        return LoginResponseDto.builder()
                .tempToken(tempToken)
                .signupStatus(signupStatus)
                .build();
    }
}
