package teamdevhub.devhub.adapter.out.mail;

import teamdevhub.devhub.adapter.out.mail.entity.EmailCertificationEntity;
import teamdevhub.devhub.port.out.mail.EmailCertificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class EmailCertificationAdapter implements EmailCertificationPort {

    private final EmailCertificationRepositoryJpa emailCertificationRepositoryJpa;

    @Override
    public void save(String email, String code, Duration limit) {
        LocalDateTime expiredAt = LocalDateTime.now().plus(limit);
        EmailCertificationEntity entity = EmailCertificationEntity.builder()
                        .email(email)
                        .code(code)
                        .expiredAt(expiredAt)
                        .build();
        emailCertificationRepositoryJpa.save(entity);
    }

    @Override
    public boolean existsValidCode(String email) {
        return emailCertificationRepositoryJpa.findById(email)
                .filter(entity -> !entity.isExpired(LocalDateTime.now()))
                .isPresent();
    }

    @Override
    public boolean verify(String email, String code) {
        EmailCertificationEntity emailCertificationEntity = emailCertificationRepositoryJpa.findById(email).orElse(null);
        if (emailCertificationEntity == null) {
            return false;
        }
        if (emailCertificationEntity.isExpired(LocalDateTime.now())) {
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
        return emailCertificationRepositoryJpa.findById(email)
                .map(entity -> entity.getVerifiedAt() != null && entity.getExpiredAt().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    @Override
    public void delete(String email) {
        emailCertificationRepositoryJpa.deleteById(email);
    }
}