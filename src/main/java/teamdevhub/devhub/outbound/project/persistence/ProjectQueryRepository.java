package teamdevhub.devhub.outbound.project.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;

public interface ProjectQueryRepository {

    Page<ProjectDetail> listProject(SearchProjectListCommand searchProjectListCommand, Pageable pageable);
    ProjectDetail findProjectDetailByGuid(String projectGuid);

}
