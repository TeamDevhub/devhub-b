package teamdevhub.devhub.core.file.application;

public record FileMetadata(
        String fileGuid,
        String originalName,
        String extensionName,
        String path,
        long size
) {

    public static FileMetadata create(
            String fileGuid,
            String originalName,
            String extensionName,
            String path,
            long size
    ) {
        validate(originalName, extensionName, path, size);
        return new FileMetadata(fileGuid, originalName, extensionName, path, size);
    }

    private static void validate(
            String originalName,
            String extensionName,
            String path,
            long size
    ) {
        if (originalName == null || originalName.isBlank()) {
            throw new IllegalArgumentException("파일명은 필수입니다.");
        }

        if (extensionName == null || extensionName.isBlank()) {
            throw new IllegalArgumentException("확장자는 필수입니다.");
        }
        
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("저장경로는 필수입니다.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("파일 크기는 0보다 커야 합니다.");
        }
    }
}
