package teamdevhub.devhub.core.project.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectRequirementRequestCommand;
import teamdevhub.devhub.core.project.domain.vo.requirement.ProjectRequirement;
import teamdevhub.devhub.core.project.domain.vo.skill.ProjectSkill;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
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
	
	@Override
	public void createProject(CreateProjectCommand createProjectCommand) {
		Project project = createGeneralProject(createProjectCommand);
		saveProjectSkills(project.getProjectGuid(), createProjectCommand.skillList());
		saveProjectRequirement(project.getProjectGuid(), createProjectCommand.positionList());
		projectRepository.save(project);
	}

	private Project createGeneralProject(CreateProjectCommand createProjectCommand) {
		String projectGuid = identifierProvider.generateIdentifier();
		return Project.createProject(createProjectCommand, projectGuid);
		
	}
	
	private void saveProjectSkills(String projectGuid, List<String> skillList) {
		Set<ProjectSkill> skills = skillList.stream()
				.map(skill -> new ProjectSkill(projectGuid, skill))
				.collect(Collectors.toUnmodifiableSet());
		projectSkillRepository.saveAll(skills);
	}

	private void saveProjectRequirement(String projectGuid, List<CreateProjectRequirementRequestCommand> positionList) {
		Set<ProjectRequirement> positions = positionList.stream()
				.map(position -> new ProjectRequirement(projectGuid, position.getPosition(), position.getLevel(), position.getCapacity()))
				.collect(Collectors.toUnmodifiableSet());
		projectRequirementRepository.saveAll(positions);
	}


	@Override
	public PageResult<ProjectDetail> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand) {
		return projectRepository.getProjectList(searchProjectListCommand, pageCommand);
	}
}
