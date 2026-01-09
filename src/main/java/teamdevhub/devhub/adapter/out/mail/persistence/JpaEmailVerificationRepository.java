package teamdevhub.devhub.adapter.out.mail.persistence;

import teamdevhub.devhub.adapter.out.mail.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmailVerificationRepository extends JpaRepository<EmailVerificationEntity, String> {
}
