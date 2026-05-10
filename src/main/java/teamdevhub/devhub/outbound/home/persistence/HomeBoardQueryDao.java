package teamdevhub.devhub.outbound.home.persistence;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HomeBoardQueryDao {

    List<Object[]> findPopularBoards(Pageable pageable, boolean sortByLike);
}
