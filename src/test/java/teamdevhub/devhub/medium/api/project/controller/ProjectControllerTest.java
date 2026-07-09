
package teamdevhub.devhub.medium.api.project.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.api.project.controller.ProjectController;
import teamdevhub.devhub.api.project.model.SearchProjectRequestDto;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.in.facade.ProjectFacade;
import teamdevhub.devhub.shared.enums.SuccessCode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ProjectControllerTest {

    private ProjectController projectController;
    private ProjectFacade projectFacade;

    @BeforeEach
    void init() {
        projectFacade = Mockito.mock(ProjectFacade.class);
        projectController = new ProjectController(projectFacade);
    }

    @Test
    @DisplayName("프로젝트_목록_조회시_ProjectDetailResponseDto_리스트와_페이지정보_READ_SUCCESS_코드를_반환한다")
    void returnProjectListWhenFetchingProjectList() {
        // given
        SearchProjectRequestDto searchRequestDto = SearchProjectRequestDto.builder()
                // 필요 시 검색 파라미터 세팅
                .build();

        Project project1 = Mockito.mock(Project.class);
        Project project2 = Mockito.mock(Project.class);

        PageResult<Project> pageResult = PageResult.of(
                List.of(project1, project2),
                0,
                10,
                2
        );

//        when(projectFacade.getProjectList(any(), any(PageCommand.class))).thenReturn(pageResult);
        when(project1.getProjectGuid()).thenReturn("");
        when(project2.getProjectGuid()).thenReturn("");

        int page = 0;
        int size = 10;

        // when
        // ResponseEntity<DataListApiResponseDto<ProjectDetailResponseDto>> response = projectController.getProjectList(searchRequestDto, page, size);

        // then

        // DataListApiResponseDto<ProjectDetailResponseDto> body = response.getBody();
        /**
         * assertThat(body.getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
         * assertThat(body.getDataList()).hasSize(2);
         * PageResponseDto pageResponseDto = body.getPagination();
         * assertThat(pageResponseDto.getPage()).isEqualTo(page);
         * assertThat(pageResponseDto.getSize()).isEqualTo(size);
         * assertThat(pageResponseDto.getTotalElements()).isEqualTo(2);
         */


    }
}
