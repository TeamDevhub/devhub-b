package teamdevhub.devhub.outbound.project.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import teamdevhub.devhub.outbound.project.adapter.entity.ProjectSkillEntity;

public interface JpaProjectSkillRepository extends JpaRepository<ProjectSkillEntity, String> {

}
