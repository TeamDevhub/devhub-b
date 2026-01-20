package teamdevhub.devhub.domain.auth.vo.token;

public record RefreshTokenInfo(String userGuid, String token) {

    public static RefreshTokenInfo of(String userGuid, String token) {
        return new RefreshTokenInfo(userGuid, token);
    }
}
