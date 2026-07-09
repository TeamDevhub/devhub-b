package teamdevhub.devhub.outbound.file.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.file.adapter.entity.FileEntity;

import java.util.Optional;

public interface JpaFileRepository extends JpaRepository<FileEntity, String> {

    Optional<FileEntity> findByFileGuid(String fileGuid);
    void deleteByFileGuid(String fileGuid);
}
