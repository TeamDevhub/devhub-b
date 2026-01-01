package teamdevhub.devhub.port.in.auth;

public interface RefreshTokenUseCase {
    void issueRefreshToken(String email, String refreshToken);
}
