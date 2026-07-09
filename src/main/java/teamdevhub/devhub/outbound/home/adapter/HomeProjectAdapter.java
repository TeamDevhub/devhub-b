package teamdevhub.devhub.outbound.home.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.home.domain.policy.ProjectExposurePolicy;
import teamdevhub.devhub.core.home.port.in.query.HomeProjectQuery;
import teamdevhub.devhub.core.home.port.out.LoadHomeProjectPort;
import teamdevhub.devhub.outbound.home.persistence.HomeProjectQueryDao;
import teamdevhub.devhub.outbound.home.persistence.HomeProjectQueryDao.HomeProjectDto;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HomeProjectAdapter implements LoadHomeProjectPort {

    private final HomeProjectQueryDao homeProjectQueryDao;

    @Override
    public List<HomeProjectResult> loadRecentProjects(HomeProjectQuery query) {
        LocalDate today = LocalDate.now();
        return homeProjectQueryDao.findHomeProjects(today, PageRequest.of(0, query.limit()))
                .stream()
                .map(dto -> new HomeProjectResult(
                        dto.projectGuid(),
                        dto.title(),
                        dto.category(),
                        dto.username(),
                        dto.imageFileGuid(),
                        dto.recruitmentStartDate() != null ? dto.recruitmentStartDate().toString() : null,
                        dto.recruitmentEndDate() != null ? dto.recruitmentEndDate().toString() : null,
                        resolveRecruitStatus(dto, today),
                        false
                ))
                .toList();
    }

    private String resolveRecruitStatus(HomeProjectDto dto, LocalDate today) {
        if (dto.recruitmentStartDate() == null || dto.recruitmentEndDate() == null) {
            return "UNKNOWN";
        }
        boolean active = ProjectExposurePolicy.isActiveRecruitment(
                dto.recruitmentStartDate(), dto.recruitmentEndDate(), today);
        return active ? "RECRUITING" : "COMPLETED";
    }
}
