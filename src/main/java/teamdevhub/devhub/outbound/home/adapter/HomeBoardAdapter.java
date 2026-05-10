package teamdevhub.devhub.outbound.home.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.home.port.in.query.HomeBoardQuery;
import teamdevhub.devhub.core.home.port.out.LoadHomeBoardPort;
import teamdevhub.devhub.outbound.home.persistence.HomeBoardQueryDao;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HomeBoardAdapter implements LoadHomeBoardPort {

    private final HomeBoardQueryDao homeBoardQueryDao;

    @Override
    public List<HomeBoardResult> loadPopularBoards(HomeBoardQuery query) {
        boolean sortByLike = HomeBoardQuery.BoardSortType.LIKE_COUNT.equals(query.sortType());
        return homeBoardQueryDao.findPopularBoards(PageRequest.of(0, query.limit()), sortByLike)
                .stream()
                .map(row -> new HomeBoardResult(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        row[4] != null ? ((Number) row[4]).intValue() : 0,
                        row[5] != null ? ((Number) row[5]).longValue() : 0L,
                        row[6] != null ? row[6].toString() : null
                ))
                .toList();
    }
}
