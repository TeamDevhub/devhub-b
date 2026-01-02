package teamdevhub.devhub.adapter.out.mail;

import teamdevhub.devhub.adapter.out.mail.entity.EmailCertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmailCertificationRepository extends JpaRepository<EmailCertificationEntity, String> {
}
