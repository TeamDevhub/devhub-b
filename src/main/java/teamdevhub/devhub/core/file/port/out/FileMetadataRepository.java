package teamdevhub.devhub.core.file.port.out;

import teamdevhub.devhub.core.file.application.StoredFile;

public interface FileMetadataRepository {

    StoredFile save(StoredFile storedFile);
    void deleteByFileGuid(String fileGuid);
}
