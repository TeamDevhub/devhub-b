package teamdevhub.devhub.outbound.project.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;

public interface ProjectQueryDao {

    Page<Project> listProject(SearchProjectListCommand searchProjectListCommand, Pageable pageable);
    Project findProjectDetailByGuid(String projectGuid);

}
