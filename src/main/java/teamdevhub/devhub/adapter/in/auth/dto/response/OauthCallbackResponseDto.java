package teamdevhub.devhub.adapter.in.auth.dto.response;

import teamdevhub.devhub.common.enums.SignupStatus;

public record OauthCallbackResponseDto(SignupStatus signupStatus, String tempToken) {

    public static OauthCallbackResponseDto existedUser(String tempToken) {
        return new OauthCallbackResponseDto(SignupStatus.COMPLETED, tempToken);
    }

    public static OauthCallbackResponseDto requiresSignupUser(String tempToken) {
        return new OauthCallbackResponseDto(SignupStatus.PENDING, tempToken);
    }
}
