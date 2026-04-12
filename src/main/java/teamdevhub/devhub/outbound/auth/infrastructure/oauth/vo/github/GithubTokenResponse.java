package teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.github;

public record GithubTokenResponse(
        String access_token,
        String token_type,
        String scope
) {}