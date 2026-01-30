package teamdevhub.devhub.core.project.port.in.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectWithoutFilesGuidCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectCreateFacade {
	
	private final ProjectUseCase projectUseCase;
//	private final FileUseCase fileUseCase;
	
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
