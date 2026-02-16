package teamdevhub.devhub.core.project.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.application.port.in.command.CreateProjectApplicationFormCommand;
import teamdevhub.devhub.core.application.port.out.ProjectApplicationFormRepository;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectSkillCommand;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectRequirementCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.core.project.port.out.ProjectRepository;
import teamdevhub.devhub.core.project.port.out.ProjectRequirementRepository;
import teamdevhub.devhub.core.project.port.out.ProjectSkillRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService implements ProjectUseCase {

	private final IdentifierProvider identifierProvider;
	private final ProjectRepository projectRepository;
	private final ProjectSkillRepository projectSkillRepository;
	private final ProjectRequirementRepository projectRequirementRepository;
	private final ProjectApplicationFormRepository projectApplicationFormRepository;
	
	@Override
	public void createProject(CreateProjectCommand createProjectCommand) {
		Project project = createGeneralProject(createProjectCommand);
		saveProjectSkills(project.getProjectGuid(), createProjectCommand.skillList());
		saveProjectRequirement(project.getProjectGuid(), createProjectCommand.positionList());
		saveProjectApplicationForm(project.getProjectGuid(), createProjectCommand.applicationFormList());
		projectRepository.save(project);
	}

	private Project createGeneralProject(CreateProjectCommand createProjectCommand) {
		String projectGuid = identifierProvider.generateIdentifier();
		return Project.createProject(createProjectCommand, projectGuid);
		
	}
	
	private void saveProjectSkills(String projectGuid, List<String> skillList) {
		Set<CreateProjectSkillCommand> skills = skillList.stream()
				.map(skill -> new CreateProjectSkillCommand(projectGuid, skill))
				.collect(Collectors.toUnmodifiableSet());
		projectSkillRepository.saveAll(skills);
	}

	private void saveProjectRequirement(String projectGuid, List<CreateProjectRequirementCommand> positionList) {
		Set<teamdevhub.devhub.core.project.domain.vo.command.CreateProjectRequirementCommand> positions = positionList.stream()
				.map(position -> new teamdevhub.devhub.core.project.domain.vo.command.CreateProjectRequirementCommand(projectGuid, position.position(), position.level(), position.capacity()))
				.collect(Collectors.toUnmodifiableSet());
		projectRequirementRepository.saveAll(positions);
	}
	

	private void saveProjectApplicationForm(String projectGuid, List<String> applicationFormList) {
		Set<CreateProjectApplicationFormCommand> forms = applicationFormList.stream()
				.map(form -> new CreateProjectApplicationFormCommand(projectGuid, form))
				.collect(Collectors.toUnmodifiableSet());
		projectApplicationFormRepository.saveAll(forms);
		
	}
	
	@Override
	public Project getProjectDetail(String projectGuid) {
		return projectRepository.getProjectDetail(projectGuid);
	}

}
