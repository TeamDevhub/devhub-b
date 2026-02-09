package teamdevhub.devhub.outbound.admin.form.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormItemEntity;

public interface JpaApplicationFormItemRepository extends JpaRepository<ApplicationFormItemEntity, String> {

}
