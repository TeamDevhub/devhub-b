package teamdevhub.devhub.outbound.application.adapter;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.application.domain.ProjectApplicationForm;
import teamdevhub.devhub.core.application.port.in.command.CreateProjectApplicationFormCommand;
import teamdevhub.devhub.core.application.port.out.ProjectApplicationFormRepository;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationFormEntity;
import teamdevhub.devhub.outbound.application.adapter.mapper.ProjectApplicationFormMapper;
import teamdevhub.devhub.outbound.application.persistence.JpaProjectApplicationFormRepository;

@Component
@RequiredArgsConstructor
public class ProjectApplicationFormAdapter implements ProjectApplicationFormRepository {
	
	private final IdentifierProvider identifierProvider;
	private final JpaProjectApplicationFormRepository jpaProjectApplicationFormRepository;

	@Override
	public void saveAll(Set<CreateProjectApplicationFormCommand> formCommands) {
		List<ProjectApplicationFormEntity> entityList = formCommands.stream()
				.map(formCommand -> {
					String projectApplicationFormGuid = identifierProvider.generateIdentifier();
					ProjectApplicationForm projectApplicationForm = ProjectApplicationForm.createProjectApplicationForm(projectApplicationFormGuid, formCommand);
					return ProjectApplicationFormMapper.toEntity(projectApplicationForm);
				})
				.toList();
		jpaProjectApplicationFormRepository.saveAll(entityList);

	}

	@Override
	public void deleteByProjectGuid(String projectGuid) {
		jpaProjectApplicationFormRepository.deleteAllByProjectGuid(projectGuid);
	}

	@Override
	public List<String> findAllGuidByProjectGuid(String projectGuid) {
		return jpaProjectApplicationFormRepository.findAllGuidByProjectGuid(projectGuid);
	}

}
