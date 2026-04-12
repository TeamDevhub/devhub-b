package teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.github;

public record GithubEmailResponse(
        String email,
        boolean primary,
        boolean verified
) {}