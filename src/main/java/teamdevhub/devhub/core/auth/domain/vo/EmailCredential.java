package teamdevhub.devhub.core.auth.domain.vo;

public record EmailCredential(
        String userGuid,
        String email,
        String password
) {}