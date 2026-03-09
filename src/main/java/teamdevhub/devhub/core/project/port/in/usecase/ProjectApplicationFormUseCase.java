package teamdevhub.devhub.core.project.port.in.usecase;

import java.util.List;


public interface ProjectApplicationFormUseCase {

	List<String> getByProjectGuid(String projectGuid);

}
