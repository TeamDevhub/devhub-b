package teamdevhub.devhub.outbound.auth.infrastructure.oauth.github.vo;

public record GithubTokenResponse(
        String access_token,
        String token_type,
        String scope
) {}