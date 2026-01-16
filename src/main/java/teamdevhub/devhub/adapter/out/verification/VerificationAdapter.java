package teamdevhub.devhub.adapter.out.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import teamdevhub.devhub.adapter.out.verification.mapper.VerificationMapper;
import teamdevhub.devhub.adapter.out.verification.persistence.JpaVerificationRepository;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.VerificationTarget;
import teamdevhub.devhub.port.out.verification.VerificationRepository;

@Repository
@RequiredArgsConstructor
public class VerificationAdapter implements VerificationRepository {

    private final JpaVerificationRepository jpaVerificationRepository;

    @Override
    public Verification findByTarget(VerificationTarget target) {
        return jpaVerificationRepository
                .findByTargetTypeAndTargetValue(
                        target.type(),
                        target.value()
                )
                .map(VerificationMapper::toDomain)
                .orElseThrow();
    }

    @Override
    public void save(Verification verification) {
        jpaVerificationRepository.save(
                VerificationMapper.toEntity(verification)
        );
    }

    @Override
    public void deleteByTarget(VerificationTarget target) {
        jpaVerificationRepository.deleteByTargetTypeAndTargetValue(
                target.type(),
                target.value()
        );
    }
}