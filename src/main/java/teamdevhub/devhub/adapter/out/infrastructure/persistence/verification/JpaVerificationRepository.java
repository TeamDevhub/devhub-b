package teamdevhub.devhub.adapter.out.infrastructure.persistence.verification;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.adapter.out.verification.entity.VerificationEntity;
import teamdevhub.devhub.domain.verification.vo.VerificationType;

import java.util.Optional;

public interface JpaVerificationRepository extends JpaRepository<VerificationEntity, Long> {

    Optional<VerificationEntity> findByVerificationTypeAndTargetValue(VerificationType verificationType, String targetValue);
    void deleteByVerificationTypeAndTargetValue(VerificationType verificationType, String targetValue);
}
