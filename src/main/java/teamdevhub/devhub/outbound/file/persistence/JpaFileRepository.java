package teamdevhub.devhub.outbound.file.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.file.adapter.entity.FileEntity;

public interface JpaFileRepository extends JpaRepository<FileEntity, String> {

    void deleteByFileGuid(String fileGuid);
}
