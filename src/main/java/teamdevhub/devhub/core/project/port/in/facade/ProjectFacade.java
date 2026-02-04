package teamdevhub.devhub.core.project.port.in.facade;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectWithoutFilesGuidCommand;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;

@Service
@RequiredArgsConstructor
public class ProjectFacade {
	
	private final ProjectUseCase projectUseCase;

	public PageResult<ProjectDetail> getProjectList(SearchProjectListCommand projectListSearchRequestDto, PageCommand pageCommand) {
		PageResult<ProjectDetail> result = projectUseCase.getProjectList(projectListSearchRequestDto, pageCommand);
		
		return result;
	}
	
	public void createProject(CreateProjectWithoutFilesGuidCommand createProjectWithUserGuidCommand, MultipartFile attachment, MultipartFile image) {
//		if (attachment != null && !attachment.isEmpty()) {
//			 File attachmentFile = fileUseCase.save(attachment);
//	    }
//		if (image != null && !image.isEmpty()) {
//			 File image = fileUseCase.save(image);
//	    }
//		projectUseCase.createProject(createProjectWithUserGuidCommand, attachmentFile.getFileGuid, image.getFileGuid);
		projectUseCase.createProject(createProjectWithUserGuidCommand.toCreateProjectCommand("testFileGuid01234546789876543223", "testFileGuid01234546789876543210"));
	}

}
