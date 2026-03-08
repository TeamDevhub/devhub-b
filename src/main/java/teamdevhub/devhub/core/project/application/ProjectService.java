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
import teamdevhub.devhub.core.project.domain.ProjectRequirement;
import teamdevhub.devhub.core.project.domain.ProjectSkill;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectRequirementCommand;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectSkillCommand;
import teamdevhub.devhub.core.project.domain.vo.command.UpdateProjectCommand;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectRequirementRequestCommand;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.core.project.port.out.ProjectLikeRepository;
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
	private final ProjectLikeRepository projectLikeRepository;
	
	@Override
	public void createProject(CreateProjectCommand createProjectCommand, String username) {
		Project project = createGeneralProject(createProjectCommand, username);
		saveProjectSkills(project.getProjectGuid(), createProjectCommand.skillList());
		saveProjectRequirement(project.getProjectGuid(), createProjectCommand.positionList());
		saveProjectApplicationForm(project.getProjectGuid(), createProjectCommand.applicationFormList());
		projectRepository.save(project);
	}

	private Project createGeneralProject(CreateProjectCommand createProjectCommand, String username) {
		String projectGuid = identifierProvider.generateIdentifier();
		return Project.createProject(createProjectCommand, projectGuid, username);
		
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
		PageResult<Project> pagedProjectList = projectRepository.getProjectList(searchProjectListCommand, pageCommand);
		Set<String> projectGuids = pagedProjectList.content().stream()
		        .map(Project::getProjectGuid)
		        .collect(Collectors.toSet());
		Map<String, List<String>> mapSKill = ProjectMapper.toMapSkill(projectSkillRepository.findByProjectGuids(projectGuids));
		Map<String, List<ProjectRequirement>> mapRequirement = ProjectMapper.toMapRequirement(projectRequirementRepository.findByProjectGuids(projectGuids));
		Map<String, String> mapLikeCount = ProjectMapper.toMapLikeCount(projectLikeRepository.findByProjectGuid(projectGuids));
		
		List<Project> content = pagedProjectList.content().stream()
				.map(project -> ProjectMapper.toProjectDetail(
						project,
						mapSKill.getOrDefault(project.getProjectGuid(), List.of()),
						mapRequirement.getOrDefault(project.getProjectGuid(), List.of()),
						 mapLikeCount.getOrDefault(project.getProjectGuid(), String.valueOf(0))))
				.toList();
		return PageResult.of(
				content,
        		pagedProjectList.page(),
        		pagedProjectList.size(),
        		pagedProjectList.totalElements());
    }

	@Override
	public Project getProjectDetail(String projectGuid) {
		Project project = projectRepository.getProjectDetail(projectGuid);
		List<String> skillList = projectSkillRepository.findByProjectGuid(projectGuid).stream()
									.map(ProjectSkill::getSkillCd)
									.toList();
		List<ProjectRequirement> requirementList = projectRequirementRepository.findByProjectGuid(projectGuid);
		int likeCount = projectLikeRepository.countByProjectGuid(projectGuid);
		
		return ProjectMapper.toProject(project, skillList, requirementList, String.valueOf(likeCount));		
	}

	@Override
	public List<String> deleteProject(String projectGuid) {
		projectSkillRepository.deleteByProjectGuid(projectGuid);
		projectRequirementRepository.deleteByProjectGuid(projectGuid);
		projectApplicationFormRepository.deleteByProjectGuid(projectGuid);
		projectLikeRepository.deleteByProjectGuid(projectGuid);
		projectRepository.deleteById(projectGuid);
		return projectApplicationFormRepository.findAllByProjectGuid(projectGuid);
	}

	@Override
	public List<String> updateProject(String projectGuid, UpdateProjectCommand updateProjectCommand) {
		List<String> deleteAppplicationFormGuids = projectApplicationFormRepository.findAllByProjectGuid(projectGuid);
		projectSkillRepository.deleteByProjectGuid(projectGuid);
		projectRequirementRepository.deleteByProjectGuid(projectGuid);
		projectApplicationFormRepository.deleteByProjectGuid(projectGuid);
		// 신청양식 변경되면 어떻게???
		// 일단 삭제 후 생성
		saveProjectSkills(projectGuid, updateProjectCommand.skillList());
		saveProjectRequirement(projectGuid, updateProjectCommand.positionList());
		saveProjectApplicationForm(projectGuid, updateProjectCommand.applicationFormList());
		projectRepository.update(Project.createUpateProject(updateProjectCommand, projectGuid));
		return deleteAppplicationFormGuids;
	}

}
