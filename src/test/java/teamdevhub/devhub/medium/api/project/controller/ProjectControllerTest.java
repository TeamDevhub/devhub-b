
package teamdevhub.devhub.medium.api.project.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import teamdevhub.devhub.api.project.controller.ProjectController;
import teamdevhub.devhub.api.project.model.request.ProjectListSearchRequestDto;
import teamdevhub.devhub.api.project.model.response.ProjectDetailResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.port.in.facade.ProjectFacade;
import teamdevhub.devhub.shared.enums.SuccessCode;

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
        ProjectListSearchRequestDto searchRequestDto = ProjectListSearchRequestDto.builder()
                // 필요 시 검색 파라미터 세팅
                .build();

        ProjectDetail project1 = Mockito.mock(ProjectDetail.class);
        ProjectDetail project2 = Mockito.mock(ProjectDetail.class);

        PageResult<ProjectDetail> pageResult = PageResult.of(
                List.of(project1, project2),
                0,
                10,
                2
        );

        when(projectFacade.getProjectList(any(), any(PageCommand.class))).thenReturn(pageResult);
        when(project1.getProject().getProjectGuid()).thenReturn("");
        when(project2.getProject().getProjectGuid()).thenReturn("");

        int page = 0;
        int size = 10;

        // when
        ResponseEntity<DataListApiResponseDto<ProjectDetailResponseDto>> response = projectController.getProjectList(searchRequestDto, page, size);

        // then
        DataListApiResponseDto<ProjectDetailResponseDto> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
        assertThat(body.getDataList()).hasSize(2);
        // 필요하면 내용 값 더 체크

        PageResponseDto pageResponseDto = body.getPagination();
        assertThat(pageResponseDto.getPage()).isEqualTo(page);
        assertThat(pageResponseDto.getSize()).isEqualTo(size);
        assertThat(pageResponseDto.getTotalElements()).isEqualTo(2);
    }
}
