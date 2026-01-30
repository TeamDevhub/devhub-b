package teamdevhub.devhub.core.project.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.out.UserRepository;
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService implements ProjectUseCase {

	private final IdentifierProvider identifierProvider;
	private final UserRepository userRepository;
	
	@Override
	public void createProject(CreateProjectCommand createProjectCommand, MultipartFile attachment, MultipartFile image) {
		String attachmentFileGuid = saveAttachmentFile(attachment);
		String imageFileGuid = saveImageFile(image);
		Project project = createGeneralProject(createProjectCommand);
		saveProjectSkills(project.getProjectGuid(), createProjectCommand.skillList());
	}

	private Project createGeneralProject(CreateProjectCommand createProjectCommand) {
		String projectGuid = identifierProvider.generateIdentifier();
		User user = userRepository.findByUserGuid(createProjectCommand.userGuid());
		return Project.createProject(createProjectCommand, projectGuid, user.getUsername());
		
	}

	private String saveImageFile(MultipartFile image) {
		// TODO Auto-generated method stub
		return null;
	}

	private String saveAttachmentFile(MultipartFile attachment) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void saveProjectSkills(String projectGuid, List<String> skillList) {
	}
}
