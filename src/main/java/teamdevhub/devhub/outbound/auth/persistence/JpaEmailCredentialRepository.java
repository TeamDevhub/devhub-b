package teamdevhub.devhub.outbound.auth.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.auth.adapter.entity.EmailCredentialEntity;

import java.util.Optional;

public interface JpaEmailCredentialRepository extends JpaRepository<EmailCredentialEntity, Long> {

    Optional<EmailCredentialEntity> findByEmail(String email);

    Optional<EmailCredentialEntity> findByUserGuid(String userGuid);
}