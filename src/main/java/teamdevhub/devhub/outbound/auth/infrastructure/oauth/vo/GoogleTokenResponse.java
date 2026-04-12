package teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo;

public record GoogleTokenResponse(
        String access_token,
        String token_type,
        Integer expires_in,
        String scope
) {}
