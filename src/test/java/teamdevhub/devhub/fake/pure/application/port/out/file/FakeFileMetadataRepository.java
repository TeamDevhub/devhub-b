package teamdevhub.devhub.fake.pure.application.port.out.file;

import teamdevhub.devhub.core.file.application.StoredFile;
import teamdevhub.devhub.core.file.port.out.FileMetadataRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeFileMetadataRepository implements FileMetadataRepository {

    private final Map<String, StoredFile> store = new HashMap<>();

    @Override
    public StoredFile save(StoredFile storedFile) {
        store.put(storedFile.fileGuid(), storedFile);
        return storedFile;
    }

    @Override
    public StoredFile find(String fileGuid) {
        return null;
    }

    @Override
    public void deleteByFileGuid(String fileGuid) {
        store.remove(fileGuid);
    }

    public Optional<StoredFile> findByFileGuid(String fileGuid) {
        return Optional.ofNullable(store.get(fileGuid));
    }
}
