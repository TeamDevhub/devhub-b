package teamdevhub.devhub.outbound.auth.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.auth.adapter.entity.VerificationEntity;
import teamdevhub.devhub.core.auth.domain.vo.VerificationType;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaVerificationRepository extends JpaRepository<VerificationEntity, Long> {

    boolean existsByVerificationTypeAndTargetValueAndExpiredAtAfterAndVerifiedFalse(VerificationType verificationType, String targetValue, LocalDateTime now);
    Optional<VerificationEntity> findTopByVerificationTypeAndTargetValueOrderByExpiredAtDesc(VerificationType verificationType, String targetValue);
    void deleteByVerificationTypeAndTargetValue(VerificationType verificationType, String targetValue);
}
