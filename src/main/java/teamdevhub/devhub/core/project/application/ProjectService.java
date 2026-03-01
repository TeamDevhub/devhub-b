package teamdevhub.devhub.core.project.application;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.application.port.in.command.CreateProjectApplicationFormCommand;
import teamdevhub.devhub.core.application.port.out.ProjectApplicationFormRepository;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.Requirement;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectRequirementCommand;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectSkillCommand;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectRequirementRequestCommand;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.core.project.port.out.ProjectQueryRepository;
import teamdevhub.devhub.core.project.port.out.ProjectRepository;
import teamdevhub.devhub.core.project.port.out.ProjectRequirementRepository;
import teamdevhub.devhub.core.project.port.out.ProjectSkillRepository;
import teamdevhub.devhub.outbound.project.adapter.mapper.ProjectMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService implements ProjectUseCase {

	private final IdentifierProvider identifierProvider;
	private final ProjectRepository projectRepository;
	private final ProjectSkillRepository projectSkillRepository;
	private final ProjectRequirementRepository projectRequirementRepository;
	private final ProjectApplicationFormRepository projectApplicationFormRepository;
	private final ProjectQueryRepository projectQueryRepository;
	
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

	private void saveProjectRequirement(String projectGuid, List<CreateProjectRequirementRequestCommand> positionList) {
		Set<CreateProjectRequirementCommand> positions = positionList.stream()
				.map(position -> new CreateProjectRequirementCommand(projectGuid, position.position(), position.level(), position.capacity()))
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
	public PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand,
			PageCommand pageCommand) {
		PageResult<Project> pagedProjectList = projectQueryRepository.getProjectList(searchProjectListCommand, pageCommand);
		Set<String> projectGuids = pagedProjectList.content().stream()
		        .map(Project::getProjectGuid)
		        .collect(Collectors.toSet());
		Map<String, List<String>> mapSKill = ProjectMapper.toMapSkill(projectSkillRepository.findByProjectGuid(projectGuids));
		Map<String, List<Requirement>> mapRequirement = ProjectMapper.toMapRequirement(projectRequirementRepository.findByProjectGuid(projectGuids));
		
		return pagedProjectList.content().stream()
				.map(project -> ProjectMapper.toProjectDetail(project, mapSKill.getOrDefault(project.getProjectGuid(), List.of()), mapRequirement.getOrDefault(project.getProjectGuid(), List.of())))
				.collect(null);
	}

	private List<String> selectProjectGuidBySkillCd(List<String> skillCodeList) {
		List<String> filterdProjectGuidBySKillCode = projectSkillRepository.selectProjectGuidBySkillCd(skillCodeList);
		return filterdProjectGuidBySKillCode;
	}
	
	
	private List<String> selectProjectGuidByPositionCdAndPositionLevelCd(List<String> positionCodeList,
			List<String> positionLevelCodeList) {
		List<String> filterdProjectGuidByPosicionCdAndPositionLevelCd = projectRequirementRepository.selectProjectGuidByPositionCodeAndPositionLevel(positionCodeList, positionLevelCodeList);
		return filterdProjectGuidByPosicionCdAndPositionLevelCd;
	}

	@Override
	public Project getProjectDetail(String projectGuid) {
		return projectRepository.getProjectDetail(projectGuid);
	}

}
