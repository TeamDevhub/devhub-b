package teamdevhub.devhub.outbound.board.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import teamdevhub.devhub.outbound.board.adapter.entity.BoardEntity;

public interface JpaBoardRepository extends JpaRepository<BoardEntity, String>{
}
