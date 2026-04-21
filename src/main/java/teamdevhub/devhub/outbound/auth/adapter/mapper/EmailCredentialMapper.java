package teamdevhub.devhub.outbound.auth.adapter.mapper;

import teamdevhub.devhub.core.auth.domain.vo.user.EmailUserCredential;
import teamdevhub.devhub.outbound.auth.adapter.entity.EmailCredentialEntity;

public class EmailCredentialMapper {

    private EmailCredentialMapper() {}

    public static EmailUserCredential toDomain(EmailCredentialEntity emailCredentialEntity) {
        return new EmailUserCredential(
                emailCredentialEntity.getUserGuid(),
                emailCredentialEntity.getEmail(),
                emailCredentialEntity.getPassword(),
                emailCredentialEntity.getUserRole()
        );
    }

    public static EmailCredentialEntity toEntity(EmailUserCredential emailUserCredential) {
        return EmailCredentialEntity.builder()
                .userGuid(emailUserCredential.userGuid())
                .email(emailUserCredential.email())
                .password(emailUserCredential.password())
                .userRole(emailUserCredential.userRole())
                .build();
    }
}