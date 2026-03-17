package teamdevhub.devhub.outbound.file.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.file.port.out.FileStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    private final FileStorageProperties fileStorageProperties;

    @Override
    public String save(String fileGuid, byte[] content) {
        try {
            Path destination = fileStorageProperties.resolve(fileGuid);
            Files.createDirectories(destination.getParent());
            Files.write(destination, content);
            return destination.toString();
        } catch (IOException e) {
            throw new RuntimeException("File saveTerms failed", e);
        }
    }

    @Override
    public byte[] read(String fileGuid) {
        try {
            Path path = fileStorageProperties.resolve(fileGuid);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("File read failed", e);
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
