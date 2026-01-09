package teamdevhub.devhub.adapter.out.mail;

import teamdevhub.devhub.adapter.out.common.exception.DataAccessException;
import teamdevhub.devhub.adapter.out.mail.entity.EmailVerificationEntity;
import teamdevhub.devhub.adapter.out.mail.mapper.EmailVerificationMapper;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.domain.mail.EmailVerification;
import teamdevhub.devhub.port.out.mail.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class EmailVerificationAdapter implements EmailVerificationRepository {

    private final JpaEmailCertificationRepository jpaEmailCertificationRepository;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public EmailVerification findByEmail(String email) {
        return jpaEmailCertificationRepository.findById(email)
                .map(EmailVerificationMapper::toDomain)
                .orElseThrow(() -> DataAccessException.of(ErrorCode.READ_FAIL));
    }

    @Override
    public boolean existUnexpiredCode(String email) {
        return jpaEmailCertificationRepository.findById(email)
                .filter(emailVerificationEntity -> !emailVerificationEntity.getExpiredAt().isBefore(dateTimeProvider.now()))
                .isPresent();
    }

    @Override
    public void save(EmailVerification emailVerification) {
        EmailVerificationEntity emailVerificationEntity = EmailVerificationMapper.toEntity(emailVerification);
        jpaEmailCertificationRepository.save(emailVerificationEntity);
    }

    @Override
    public void delete(String email) {
        jpaEmailCertificationRepository.deleteById(email);
    }
}