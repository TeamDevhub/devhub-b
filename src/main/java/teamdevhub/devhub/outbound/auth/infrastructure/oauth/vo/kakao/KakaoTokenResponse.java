package teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.kakao;

public record KakaoTokenResponse(
        String access_token,
        String token_type,
        Integer expires_in
) {}
