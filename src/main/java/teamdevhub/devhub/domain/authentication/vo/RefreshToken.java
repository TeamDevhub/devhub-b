package teamdevhub.devhub.domain.authentication.vo;

public record RefreshToken(String userGuid, String token) {
    public static RefreshToken of(String userGuid, String token) {
        return new RefreshToken(userGuid, token);
    }
}
