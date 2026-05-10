package teamdevhub.devhub.outbound.home.persistence;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface HomeProjectQueryDao {

    List<Object[]> findHomeProjects(LocalDate today, Pageable pageable);
}
