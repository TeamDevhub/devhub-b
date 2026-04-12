package teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.github;

public record GithubUserResponse(
        Long id,
        String login,
        String email
) {}