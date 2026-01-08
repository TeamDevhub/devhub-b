package teamdevhub.devhub.adapter.out.mail;

import teamdevhub.devhub.adapter.out.mail.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmailCertificationRepository extends JpaRepository<EmailVerificationEntity, String> {
}
