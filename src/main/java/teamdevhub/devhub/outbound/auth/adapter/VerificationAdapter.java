package teamdevhub.devhub.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.shared.exception.AdapterDataException;
import teamdevhub.devhub.outbound.auth.adapter.mapper.VerificationMapper;
import teamdevhub.devhub.outbound.auth.persistence.JpaVerificationRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.core.auth.domain.Verification;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.auth.port.out.verification.VerificationRepository;

@Component
@RequiredArgsConstructor
public class VerificationAdapter implements VerificationRepository {

    private final JpaVerificationRepository jpaVerificationRepository;

    @Override
    public void save(Verification verification) {
        jpaVerificationRepository.save(VerificationMapper.toEntity(verification));
    }

    @Override
    public Verification findByVerificationTarget(VerificationTarget verificationTarget) {
        return jpaVerificationRepository.findByVerificationTypeAndTargetValue(verificationTarget.verificationType(), verificationTarget.value())
                .map(VerificationMapper::toDomain)
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.VERIFICATION_NOT_EXISTED));
    }

    @Override
    public void deleteByVerificationTarget(VerificationTarget verificationTarget) {
        jpaVerificationRepository.deleteByVerificationTypeAndTargetValue(verificationTarget.verificationType(), verificationTarget.value());
    }
}