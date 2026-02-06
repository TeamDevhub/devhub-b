package teamdevhub.devhub.core.file.application;

import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;

public record StoredFile(
        String fileGuid,
        String originalName,
        String extensionName,
        long size,
        String path
) {

    public static StoredFile from(String fileGuid, UploadFileCommand uploadFileCommand, String path) {
        return new StoredFile(
                fileGuid,
                uploadFileCommand.originalName(),
                uploadFileCommand.extension(),
                uploadFileCommand.size(),
                path
        );
    }
}
