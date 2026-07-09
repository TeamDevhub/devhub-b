package teamdevhub.devhub.core.file.port.out;

import teamdevhub.devhub.core.file.application.FileMetadata;

public interface FileMetadataRepository {

    FileMetadata save(FileMetadata fileMetadata);
    FileMetadata find(String fileGuid);
    void deleteByFileGuid(String fileGuid);
}
