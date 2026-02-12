package teamdevhub.devhub.core.file.port.in.facade.model;

import teamdevhub.devhub.core.file.application.FileMetadata;

public record FileResponseDto(
        String fileGuid,
        String filename,
        long size
) {
    public static FileResponseDto from(FileMetadata fileMetadata) {
        return new FileResponseDto(
                fileMetadata.fileGuid(),
                fileMetadata.originalName(),
                fileMetadata.size()
        );
    }
}