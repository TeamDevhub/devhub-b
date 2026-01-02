package teamdevhub.devhub.adapter.out.mail;

import teamdevhub.devhub.adapter.out.mail.entity.EmailCertificationEntity;
import teamdevhub.devhub.domain.record.mail.EmailCertification;
import teamdevhub.devhub.port.out.common.DateTimeProvider;
import teamdevhub.devhub.port.out.mail.EmailCertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class EmailCertificationAdapter implements EmailCertificationRepository {

    private final JpaEmailCertificationRepository jpaEmailCertificationRepository;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public void save(EmailCertification emailCertification) {
        EmailCertificationEntity entity = EmailCertificationEntity.builder()
                        .email(emailCertification.email())
                        .code(emailCertification.code())
                        .expiredAt(emailCertification.expiredAt())
                        .verifiedAt(null)
                        .build();
        jpaEmailCertificationRepository.save(entity);
    }

    @Override
    public boolean existsValidCode(String email) {
        return jpaEmailCertificationRepository.findById(email)
                .filter(entity -> !entity.isExpired(dateTimeProvider.now()))
                .isPresent();
    }

    @Override
    public boolean verify(String email, String code) {
        EmailCertificationEntity emailCertificationEntity = jpaEmailCertificationRepository.findById(email).orElse(null);
        if (emailCertificationEntity == null) {
            return false;
        }
        if (emailCertificationEntity.isExpired(dateTimeProvider.now())) {
            return false;
        }
        if (!emailCertificationEntity.getCode().equals(code)) {
            return false;
        }
        emailCertificationEntity.verify(LocalDateTime.now());
        return true;
    }

    @Override
    public boolean isVerified(String email) {
        return jpaEmailCertificationRepository.findById(email)
                .map(entity -> entity.getVerifiedAt() != null && entity.getExpiredAt().isAfter(dateTimeProvider.now()))
                .orElse(false);
    }

    @Override
    public void delete(String email) {
        jpaEmailCertificationRepository.deleteById(email);
    }
}