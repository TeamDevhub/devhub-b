package teamdevhub.devhub.outbound.auth.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.auth.adapter.entity.VerificationEntity;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationType;

import java.util.Optional;

public interface JpaVerificationRepository extends JpaRepository<VerificationEntity, Long> {

    Optional<VerificationEntity> findByVerificationTypeAndTargetValue(VerificationType verificationType, String targetValue);
    void deleteByVerificationTypeAndTargetValue(VerificationType verificationType, String targetValue);
}
