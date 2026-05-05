package teamdevhub.devhub.outbound.file.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.core.file.port.out.FileMetadataRepository;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.outbound.file.adapter.mapper.FileMetadataMapper;
import teamdevhub.devhub.outbound.file.persistence.JpaFileRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Component
@RequiredArgsConstructor
public class FileMetadataAdapter implements FileMetadataRepository {

    private final JpaFileRepository jpaFileRepository;

    @Override
    public FileMetadata save(FileMetadata fileMetadata) {
        FileEntity fileEntity = jpaFileRepository.save(FileMetadataMapper.toEntity(fileMetadata));
        return FileMetadataMapper.toDomain(fileEntity);
    }

    @Override
    public FileMetadata find(String fileGuid) {
        return jpaFileRepository.findByFileGuid(fileGuid)
                .map(FileMetadataMapper::toDomain)
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.FILE_READ_FAIL));
    }

    @Override
    public void deleteByFileGuid(String fileGuid) {
        jpaFileRepository.deleteByFileGuid(fileGuid);
    }
}
