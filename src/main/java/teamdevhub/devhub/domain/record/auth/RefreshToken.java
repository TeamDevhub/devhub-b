package teamdevhub.devhub.domain.record.auth;

public record RefreshToken(String email, String token) {
    public static RefreshToken of(String email, String token) {
        return new RefreshToken(email, token);
    }
}
