package teamdevhub.devhub.adapter.out.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.adapter.out.verification.mapper.VerificationMapper;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.verification.JpaVerificationRepository;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.out.verification.VerificationRepository;

@Component
@RequiredArgsConstructor
public class VerificationAdapter implements VerificationRepository {

    private final JpaVerificationRepository jpaVerificationRepository;

    @Override
    public Verification findByVerificationTarget(VerificationTarget verificationTarget) {
        return jpaVerificationRepository.findByVerificationTypeAndTargetValue(verificationTarget.verificationType(), verificationTarget.value())
                .map(VerificationMapper::toDomain)
                .orElseThrow();
    }

    @Override
    public void save(Verification verification) {
        jpaVerificationRepository.save(VerificationMapper.toEntity(verification));
    }

    @Override
    public void deleteByVerificationTarget(VerificationTarget target) {
        jpaVerificationRepository.deleteByVerificationTypeAndTargetValue(target.verificationType(), target.value());
    }
}