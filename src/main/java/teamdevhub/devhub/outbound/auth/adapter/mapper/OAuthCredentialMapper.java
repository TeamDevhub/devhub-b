package teamdevhub.devhub.outbound.auth.adapter.mapper;

import teamdevhub.devhub.core.auth.domain.vo.OAuthCredential;
import teamdevhub.devhub.outbound.auth.adapter.entity.OAuthCredentialEntity;

public class OAuthCredentialMapper {

    private OAuthCredentialMapper() {}

    public static OAuthCredential toDomain(OAuthCredentialEntity oAuthCredentialEntity) {
        return new OAuthCredential(
                oAuthCredentialEntity.getUserGuid(),
                oAuthCredentialEntity.getProvider(),
                oAuthCredentialEntity.getOauthId()
        );
    }

    public static OAuthCredentialEntity toEntity(OAuthCredential oAuthCredential) {
        return OAuthCredentialEntity.builder()
                .userGuid(oAuthCredential.userGuid())
                .provider(oAuthCredential.provider())
                .oauthId(oAuthCredential.oauthId())
                .build();
    }
}
