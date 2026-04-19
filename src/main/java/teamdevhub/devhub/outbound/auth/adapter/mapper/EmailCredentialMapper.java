package teamdevhub.devhub.outbound.auth.adapter.mapper;

import teamdevhub.devhub.core.auth.domain.vo.EmailCredential;
import teamdevhub.devhub.outbound.auth.adapter.entity.EmailCredentialEntity;

public class EmailCredentialMapper {

    private EmailCredentialMapper() {}

    public static EmailCredential toDomain(EmailCredentialEntity emailCredentialEntity) {
        return new EmailCredential(
                emailCredentialEntity.getUserGuid(),
                emailCredentialEntity.getEmail(),
                emailCredentialEntity.getPassword()
        );
    }

    public static EmailCredentialEntity toEntity(EmailCredential emailCredential) {
        return EmailCredentialEntity.builder()
                .userGuid(emailCredential.userGuid())
                .email(emailCredential.email())
                .password(emailCredential.password())
                .build();
    }
}