package teamdevhub.devhub.outbound.home.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.home.domain.policy.ProjectExposurePolicy;
import teamdevhub.devhub.core.home.port.in.query.HomeProjectQuery;
import teamdevhub.devhub.core.home.port.out.LoadHomeProjectPort;
import teamdevhub.devhub.outbound.home.persistence.HomeProjectQueryDao;

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
                .map(row -> new HomeProjectResult(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        (String) row[4],
                        row[5] != null ? row[5].toString() : null,
                        row[6] != null ? row[6].toString() : null,
                        resolveRecruitStatus(row, today),
                        false
                ))
                .toList();
    }

    private String resolveRecruitStatus(Object[] row, LocalDate today) {
        if (row[5] == null || row[6] == null) {
            return "UNKNOWN";
        }
        LocalDate startDate = (LocalDate) row[5];
        LocalDate endDate = (LocalDate) row[6];
        boolean active = ProjectExposurePolicy.isActiveRecruitment(startDate, endDate, today);
        return active ? "RECRUITING" : "COMPLETED";
    }
}
