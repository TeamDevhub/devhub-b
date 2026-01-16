package teamdevhub.devhub.adapter.out.verification.mapper;

import teamdevhub.devhub.adapter.out.verification.entity.VerificationEntity;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.VerificationTarget;

public class VerificationMapper {

    public static VerificationEntity toEntity(Verification domain) {
        return VerificationEntity.builder()
                .targetType(domain.target().type())
                .targetValue(domain.target().value())
                .code(domain.getCode())
                .expiredAt(domain.getExpiredAt())
                .verified(domain.isVerified())
                .build();
    }

    public static Verification toDomain(VerificationEntity entity) {
        return Verification.restore(
                VerificationTarget.restore(
                        entity.getTargetType(),
                        entity.getTargetValue()
                ),
                entity.getCode(),
                entity.getExpiredAt(),
                entity.isVerified()
        );
    }
}
