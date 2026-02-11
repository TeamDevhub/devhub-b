package teamdevhub.devhub.core.file.application;

public record FileResource(
        StoredFile metadata,
        byte[] content
) {
    public static FileResource of(StoredFile metadata, byte[] content) {
        return new FileResource(metadata, content);
    }
}
