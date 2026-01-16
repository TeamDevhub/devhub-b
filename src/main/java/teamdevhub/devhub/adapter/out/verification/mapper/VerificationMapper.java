package teamdevhub.devhub.adapter.out.verification.mapper;

import teamdevhub.devhub.adapter.out.verification.entity.VerificationEntity;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

public class VerificationMapper {

    public static VerificationEntity toEntity(Verification verification) {
        return VerificationEntity.builder()
                .id(verification.getId())
                .verificationType(verification.getVerificationTarget().type())
                .targetValue(verification.getVerificationTarget().value())
                .code(verification.getCode())
                .expiredAt(verification.getExpiredAt())
                .verified(verification.isVerified())
                .build();
    }

    public static Verification toDomain(VerificationEntity verificationEntity) {
        return Verification.restore(
                verificationEntity.getId(),
                VerificationTarget.restore(verificationEntity.getVerificationType(), verificationEntity.getTargetValue()),
                verificationEntity.getCode(),
                verificationEntity.getExpiredAt(),
                verificationEntity.isVerified()
        );
    }
}
