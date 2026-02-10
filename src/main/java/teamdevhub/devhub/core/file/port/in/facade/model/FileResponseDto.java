package teamdevhub.devhub.core.file.port.in.facade.model;

import teamdevhub.devhub.core.file.application.StoredFile;

public record FileResponseDto(
        String fileGuid,
        String filename,
        long size
) {
    public static FileResponseDto from(StoredFile storedFile) {
        return new FileResponseDto(
                storedFile.fileGuid(),
                storedFile.originalName(),
                storedFile.size()
        );
    }
}