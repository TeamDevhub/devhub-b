package teamdevhub.devhub.outbound.home.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HomeBoardQueryDaoImpl implements HomeBoardQueryDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findPopularBoards(Pageable pageable, boolean sortByLike) {
        String jpql = """
                SELECT b.boardGuid, b.title, b.categoryCd, u.username,
                       b.viewCount,
                       (SELECT COUNT(bl) FROM BoardLikeEntity bl WHERE bl.boardGuid = b.boardGuid),
                       b.registeredDate
                FROM BoardEntity b
                JOIN UserEntity u ON b.userGuid = u.userGuid
                ORDER BY
                    CASE WHEN :sortByLike = true THEN
                        (SELECT COUNT(bl2) FROM BoardLikeEntity bl2 WHERE bl2.boardGuid = b.boardGuid)
                    ELSE b.viewCount END DESC
                """;

        Query query = entityManager.createQuery(jpql)
                .setParameter("sortByLike", sortByLike)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }
}
