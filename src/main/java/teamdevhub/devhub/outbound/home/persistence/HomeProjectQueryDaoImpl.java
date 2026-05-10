package teamdevhub.devhub.outbound.home.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HomeProjectQueryDaoImpl implements HomeProjectQueryDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findHomeProjects(LocalDate today, Pageable pageable) {
        String jpql = """
                SELECT p.projectGuid, p.title, p.category, p.username,
                       p.imageFileGuid, p.recruitmentStartDate, p.recruitmentEndDate,
                       p.registeredDate
                FROM ProjectEntity p
                WHERE p.deleted = false
                ORDER BY
                    CASE WHEN p.recruitmentStartDate <= :today AND p.recruitmentEndDate >= :today THEN 0 ELSE 1 END ASC,
                    p.registeredDate DESC
                """;

        Query query = entityManager.createQuery(jpql)
                .setParameter("today", today)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }
}
