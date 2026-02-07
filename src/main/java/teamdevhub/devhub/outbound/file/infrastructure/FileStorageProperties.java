package teamdevhub.devhub.outbound.file.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "file.storage")
public record FileStorageProperties(
        Path rootPath
) {

    public Path resolve(String fileGuid) {
        String prefix = fileGuid.substring(0, 2);
        return rootPath
                .resolve(prefix)
                .resolve(fileGuid);
    }
}
