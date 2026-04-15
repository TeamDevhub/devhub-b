package teamdevhub.devhub.outbound.auth.infrastructure.oauth.github.vo;

public record GithubEmailResponse(
        String email,
        boolean primary,
        boolean verified
) {}