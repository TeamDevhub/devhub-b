package teamdevhub.devhub.outbound.terms.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.terms.adapter.entity.TermsEntity;

import java.util.Optional;

public interface JpaTermsRepository extends JpaRepository<TermsEntity, String> {

    Optional<TermsEntity> findByTermsGuid(String termsGuid);
}
