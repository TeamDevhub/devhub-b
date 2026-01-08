package teamdevhub.devhub.adapter.out.mail.mapper;

import teamdevhub.devhub.adapter.out.mail.entity.EmailVerificationEntity;
import teamdevhub.devhub.domain.mail.EmailVerification;

public class EmailVerificationMapper {

    public static EmailVerification toDomain(EmailVerificationEntity emailVerificationEntity) {
        return EmailVerification.from(
                emailVerificationEntity.getEmail(),
                emailVerificationEntity.getCode(),
                emailVerificationEntity.getExpiredAt(),
                emailVerificationEntity.isVerified()
        );
    }

    public static EmailVerificationEntity toEntity(EmailVerification emailVerification) {
        return EmailVerificationEntity.builder()
                .email(emailVerification.getEmail())
                .code(emailVerification.getCode())
                .expiredAt(emailVerification.getExpiredAt())
                .verified(emailVerification.isVerified())
                .build();
    }
}
