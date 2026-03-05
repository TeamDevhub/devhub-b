package teamdevhub.devhub.outbound.board.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.outbound.board.adapter.entity.BoardEntity;

public interface JpaBoardRepository extends JpaRepository<BoardEntity, String> {
	
	@Query(value = "select b " +
			"from BoardEntity b " +
			"where (:title IS NULL OR b.title LIKE %:title%) " +
			"AND (:categoryCd IS NULL OR b.categoryCd=:categoryCd)")
	Page<BoardEntity> findByConditions(@Param("title") String title,  @Param("categoryCd") String categoryCd, Pageable pageable);

}
