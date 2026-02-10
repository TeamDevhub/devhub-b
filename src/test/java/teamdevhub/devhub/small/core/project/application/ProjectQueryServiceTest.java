package teamdevhub.devhub.small.core.project.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.application.ProjectQueryService;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.out.ProjectQueryRepository;

public class ProjectQueryServiceTest {

    private ProjectQueryRepository projectQueryRepository;
    private ProjectQueryService projectQueryService;

    @BeforeEach
    void setUp() {
        projectQueryRepository = Mockito.mock(ProjectQueryRepository.class);
        projectQueryService = new ProjectQueryService(projectQueryRepository);
        
    }

    @Test
    @DisplayName("getProjectList는 repository 결과를 올바르게 반환한다")
    void getProjectList_returnCorrectly() {
        // given
        SearchProjectListCommand searchCommand = Mockito.mock(SearchProjectListCommand.class);
        PageCommand pageCommand = PageCommand.of(0, 5);

        Project project1 = Mockito.mock(Project.class);
        Project project2 = Mockito.mock(Project.class);
        PageResult<Project> pageResult = PageResult.of(List.of(project1, project2), 0, 5, 2);
        when(projectQueryRepository.getProjectList(any(), any())).thenReturn(pageResult);

        // when
        PageResult<Project> result = projectQueryService.getProjectList(searchCommand, pageCommand);

        // then
        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(5);
        assertThat(result.totalElements()).isEqualTo(2);
    }
}
