package teamdevhub.devhub.outbound.file.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.core.file.port.out.FileMetadataRepository;
import teamdevhub.devhub.outbound.file.adapter.entity.FileEntity;
import teamdevhub.devhub.outbound.file.adapter.mapper.FileMetaDataMapper;
import teamdevhub.devhub.outbound.file.persistence.JpaFileRepository;

@Component
@RequiredArgsConstructor
public class FileMetadataAdapter implements FileMetadataRepository {

    private final JpaFileRepository jpaFileRepository;

    @Override
    public FileMetadata save(FileMetadata fileMetadata) {
        FileEntity fileEntity = jpaFileRepository.save(FileMetaDataMapper.toEntity(fileMetadata));
        return FileMetaDataMapper.toStoredFile(fileEntity);
    }

    @Override
    public FileMetadata find(String fileGuid) {
        return jpaFileRepository.findByFileGuid(fileGuid)
                .map(FileMetaDataMapper::toStoredFile)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));
    }

    @Override
    public void deleteByFileGuid(String fileGuid) {
        jpaFileRepository.deleteByFileGuid(fileGuid);
    }
}
