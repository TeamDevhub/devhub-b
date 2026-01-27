package teamdevhub.devhub.infrastructure.auth.adapter.out.mapper;

import teamdevhub.devhub.infrastructure.auth.adapter.out.entity.VerificationEntity;
import teamdevhub.devhub.core.auth.domain.Verification;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;

public class VerificationMapper {

    public static VerificationEntity toEntity(Verification verification) {
        return VerificationEntity.builder()
                .id(verification.getId())
                .verificationType(verification.getVerificationTarget().verificationType())
                .targetValue(verification.getVerificationTarget().value())
                .code(verification.getCode())
                .expiredAt(verification.getExpiredAt())
                .verified(verification.isVerified())
                .build();
    }

    public static Verification toDomain(VerificationEntity verificationEntity) {
        return Verification.of(
                verificationEntity.getId(),
                VerificationTarget.fromEntity(verificationEntity.getVerificationType(), verificationEntity.getTargetValue()),
                verificationEntity.getCode(),
                verificationEntity.getExpiredAt(),
                verificationEntity.isVerified()
        );
    }
}
