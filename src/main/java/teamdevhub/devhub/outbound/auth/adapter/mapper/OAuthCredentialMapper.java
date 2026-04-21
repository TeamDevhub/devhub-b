package teamdevhub.devhub.outbound.auth.adapter.mapper;

import teamdevhub.devhub.core.auth.domain.vo.user.OAuthUserCredential;
import teamdevhub.devhub.outbound.auth.adapter.entity.OAuthCredentialEntity;

public class OAuthCredentialMapper {

    private OAuthCredentialMapper() {}

    public static OAuthUserCredential toDomain(OAuthCredentialEntity oAuthCredentialEntity) {
        return new OAuthUserCredential(
                oAuthCredentialEntity.getUserGuid(),
                oAuthCredentialEntity.getProvider(),
                oAuthCredentialEntity.getOauthId(),
                oAuthCredentialEntity.getUserRole()
        );
    }

    public static OAuthCredentialEntity toEntity(OAuthUserCredential oAuthUserCredential) {
        return OAuthCredentialEntity.builder()
                .userGuid(oAuthUserCredential.userGuid())
                .provider(oAuthUserCredential.provider())
                .oauthId(oAuthUserCredential.oauthId())
                .userRole(oAuthUserCredential.userRole())
                .build();
    }
}
