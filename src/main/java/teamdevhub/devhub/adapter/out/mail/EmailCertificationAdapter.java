package teamdevhub.devhub.adapter.out.mail;

import teamdevhub.devhub.adapter.out.common.exception.DataAccessException;
import teamdevhub.devhub.adapter.out.mail.entity.EmailCertificationEntity;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.common.record.mail.EmailCertification;
import teamdevhub.devhub.port.out.provider.DateTimeProvider;
import teamdevhub.devhub.port.out.mail.EmailCertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class EmailCertificationAdapter implements EmailCertificationRepository {

    private final JpaEmailCertificationRepository jpaEmailCertificationRepository;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public void save(EmailCertification emailCertification) {
        EmailCertificationEntity emailCertificationEntity = EmailCertificationEntity.builder()
                        .email(emailCertification.email())
                        .code(emailCertification.code())
                        .expiredAt(emailCertification.expiredAt())
                        .verifiedAt(null)
                        .build();
        jpaEmailCertificationRepository.save(emailCertificationEntity);
    }

    @Override
    public EmailCertification findByEmail(String email) {
        return jpaEmailCertificationRepository.findById(email)
                .map(EmailCertificationEntity::toEmailCertification)
                .orElseThrow(() -> DataAccessException.of(ErrorCode.READ_FAIL));
    }

    @Override
    public boolean existUnexpiredCode(String email) {
        return jpaEmailCertificationRepository.findById(email)
                .filter(emailCertificationEntity -> !emailCertificationEntity.isExpired(dateTimeProvider.now()))
                .isPresent();
    }

    @Override
    public void delete(String email) {
        jpaEmailCertificationRepository.deleteById(email);
    }
}