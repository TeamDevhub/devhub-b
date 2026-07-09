package teamdevhub.devhub.core.auth.application.service.token;

import lombok.Builder;

@Builder
public record RefreshToken(String userGuid, String token) {

    public static RefreshToken of(String userGuid, String token) {
        return new RefreshToken(userGuid, token);
    }

}
