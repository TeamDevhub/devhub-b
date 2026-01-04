package teamdevhub.devhub.adapter.out.mail;

import teamdevhub.devhub.adapter.out.mail.entity.EmailCertificationEntity;
import teamdevhub.devhub.domain.common.record.mail.EmailCertification;
import teamdevhub.devhub.port.out.common.DateTimeProvider;
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
    public boolean hasUnexpiredCode(String email) {
        return jpaEmailCertificationRepository.findById(email)
                .filter(emailCertificationEntity -> !emailCertificationEntity.isExpired(dateTimeProvider.now()))
                .isPresent();
    }

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
    public boolean verify(String email, String emailCertificationCode) {
        EmailCertificationEntity emailCertificationEntity = jpaEmailCertificationRepository.findById(email).orElse(null);

        if (emailCertificationEntity == null) {
            return false;
        }

        if (emailCertificationEntity.isExpired(dateTimeProvider.now())) {
            return false;
        }

        if (!emailCertificationEntity.getCode().equals(emailCertificationCode)) {
            return false;
        }

        emailCertificationEntity.verify(dateTimeProvider.now());
        return true;
    }

    @Override
    public boolean isVerified(String email) {
        return jpaEmailCertificationRepository.findById(email)
                .map(emailCertificationEntity -> emailCertificationEntity.getVerifiedAt() != null && emailCertificationEntity.getExpiredAt().isAfter(dateTimeProvider.now()))
                .orElse(false);
    }

    @Override
    public void delete(String email) {
        jpaEmailCertificationRepository.deleteById(email);
    }
}