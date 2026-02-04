package teamdevhub.devhub.core.project.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.api.project.model.response.ProjectDetailResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.shared.enums.SuccessCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectFacade {
	
	private final ProjectUseCase projectUseCase;

	public DataListApiResponseDto<ProjectDetailResponseDto> getProjectList(SearchProjectListCommand projectListSearchRequestDto, PageCommand pageCommand) {
		
		PageResult<Project> pagedProjectList = projectUseCase.getProjectList(projectListSearchRequestDto, pageCommand);
        List<ProjectDetailResponseDto> projectDetailResponseDtoList = pagedProjectList.content().stream()
                .map(ProjectDetailResponseDto::fromDomain)
                .toList();
		
		return DataListApiResponseDto.successWithDataList(
                SuccessCode.READ_SUCCESS,
                projectDetailResponseDtoList,
                PageResponseDto.from(pagedProjectList)
		);
	}

}
