package teamdevhub.devhub.small.core.project.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.core.project.application.ProjectService;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectRequirementCommand;
import teamdevhub.devhub.fake.pure.application.port.out.application.FakeProjectApplicationFormRepository;
import teamdevhub.devhub.fake.pure.application.port.out.project.FakeProjectRepository;
import teamdevhub.devhub.fake.pure.application.port.out.project.FakeProjectRequirementRepository;
import teamdevhub.devhub.fake.pure.application.port.out.project.FakeProjectSkillRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

public class ProjectServiceTest {
	
	private ProjectService projectService;
	
	private FakeUuidIdentifierProvider identifierProvider;
	private FakeProjectRepository projectRepositroy;
	private FakeProjectRequirementRepository projectRequirementRepository;
	private FakeProjectSkillRepository projectSkillRepository;
	private FakeProjectApplicationFormRepository projectApplicationFormRepository;
	
	@BeforeEach
	void init() {
		identifierProvider = new FakeUuidIdentifierProvider("PROJECT_UUID");
		projectRepositroy = new FakeProjectRepository();
		projectRequirementRepository = new FakeProjectRequirementRepository();
		projectSkillRepository = new FakeProjectSkillRepository();
		projectApplicationFormRepository = new FakeProjectApplicationFormRepository();
		
		projectService = new ProjectService(identifierProvider, projectRepositroy, projectSkillRepository, projectRequirementRepository, projectApplicationFormRepository);
	}
	
	@Test
	@DisplayName("프로젝트를 생성하면 스킬과 포지션과 신청폼이 저장된다")
	void createProject() {
		//given
		CreateProjectCommand createProjectCommand = CreateProjectCommand.builder()
				.userGuid("1")
				.username("홍길동")
				.attachmentFileGuid("2")
				.imageFileGuid("3")
				.title("프로젝트 생성 테스트 제목입니다")
				.category("001")
				.content("프로젝트 생성 테스트 내용입니다")
				.recruitmentTypeCd("002")
				.progressTypeCd("003")
				.progressRegionCd("004")
				.recruitmentStartDate(LocalDate.of(2025, 1, 1))
				.recruitmentEndDate(LocalDate.of(2025, 2, 1))
				.progressStartDate(LocalDate.of(2025, 3, 1))
				.progressEndDate(LocalDate.of(2025, 4, 1))
				.skillList(List.of("001", "002"))
				.positionList(List.of(new CreateProjectRequirementCommand("001", "002", 2)))
				.applicationFormList(List.of("002"))
				.additionalFormList(List.of(new CreateApplicationFormCommand("001", "자격증 유무", "헬프테스트", List.of("Y", "N"))))
				.build();
		//when
		projectService.createProject(createProjectCommand);
		//then
		assertThat(projectRepositroy.getProjectDetail("PROJECT_UUID").getUsername()).isEqualTo("홍길동");
		assertThat(projectRepositroy.getProjectDetail("PROJECT_UUID").getTitle()).isEqualTo("프로젝트 생성 테스트 제목입니다");
		
		assertThat(projectRequirementRepository.findByProjectGuid("PROJECT_UUID").get(0).getCapacity()).isEqualTo(2);
		
		assertThat(projectSkillRepository.findByProjectGuid("PROJECT_UUID").size()).isEqualTo(2);
		
		assertThat(projectApplicationFormRepository.findByProjectGuid("PROJECT_UUID").size()).isEqualTo(1);
	}
	
	@Test
	@DisplayName("프로젝트 상세를 조회할 수 있다")
	void getProjectDetail() {
		//given
		CreateProjectCommand createProjectCommand = CreateProjectCommand.builder()
				.userGuid("1")
				.username("홍길동")
				.attachmentFileGuid("2")
				.imageFileGuid("3")
				.title("프로젝트 생성 테스트 제목입니다")
				.category("001")
				.content("프로젝트 생성 테스트 내용입니다")
				.recruitmentTypeCd("002")
				.progressTypeCd("003")
				.progressRegionCd("004")
				.recruitmentStartDate(LocalDate.of(2025, 1, 1))
				.recruitmentEndDate(LocalDate.of(2025, 2, 1))
				.progressStartDate(LocalDate.of(2025, 3, 1))
				.progressEndDate(LocalDate.of(2025, 4, 1))
				.skillList(List.of("001", "002"))
				.positionList(List.of(new CreateProjectRequirementCommand("001", "002", 2)))
				.applicationFormList(List.of("002"))
				.additionalFormList(List.of(new CreateApplicationFormCommand("001", "자격증 유무", "헬프테스트", List.of("Y", "N"))))
				.build();
		
		//when
		projectService.createProject(createProjectCommand);
		Project result = projectService.getProjectDetail("PROJECT_UUID");
		
		//then
		assertThat(result.getUsername()).isEqualTo("홍길동");
	}
	

}
