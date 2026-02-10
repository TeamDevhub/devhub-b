package teamdevhub.devhub.outbound.project.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.out.ProjectQueryRepository;
import teamdevhub.devhub.outbound.project.persistence.ProjectQueryDao;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectQueryAdapter implements ProjectQueryRepository {

    private final ProjectQueryDao projectQueryDao;

    @Override
    public PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand) {
        Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size(), Sort.by("registeredDate").descending());

        Page<Project> pagedProjectEntityList = projectQueryDao.listProject(searchProjectListCommand, pageable);
        List<Project> projectList = pagedProjectEntityList.getContent();

        return PageResult.of(
                projectList,
                pagedProjectEntityList.getNumber(),
                pagedProjectEntityList.getSize(),
                pagedProjectEntityList.getTotalElements());
    }
}
