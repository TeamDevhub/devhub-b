package teamdevhub.devhub.outbound.terms.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.outbound.terms.adapter.entity.TermsEntity;

import java.util.Optional;

public interface JpaTermsRepository extends JpaRepository<TermsEntity, String> {

    PageResult<Terms> list(PageCommand pageCommand);
    Optional<TermsEntity> findByTermsGuid(String termsGuid);
    void save(Terms terms);
}
