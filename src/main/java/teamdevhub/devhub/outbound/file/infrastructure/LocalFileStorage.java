package teamdevhub.devhub.outbound.file.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.file.port.out.FileStorage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    private final FileStorageProperties fileStorageProperties;

    @Override
    public String save(String fileGuid, InputStream content) {
        try {
            Path destination = fileStorageProperties.resolve(fileGuid);
            Files.createDirectories(destination.getParent());
            Files.copy(content, destination, StandardCopyOption.REPLACE_EXISTING);
            return destination.toString();
        } catch (IOException e) {
            throw new RuntimeException("File save failed", e);
        }
    }

    @Override
    public void delete(String fileGuid) {
        try {
            Path destination = fileStorageProperties.resolve(fileGuid);
            Files.deleteIfExists(destination);
        } catch (IOException e) {
            throw new RuntimeException("File deleteByFileGuid failed", e);
        }
    }
}
