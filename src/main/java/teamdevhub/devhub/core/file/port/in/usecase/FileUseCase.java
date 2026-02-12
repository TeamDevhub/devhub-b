package teamdevhub.devhub.core.file.port.in.usecase;

import teamdevhub.devhub.core.file.application.FileResource;
import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;

public interface FileUseCase {

    FileMetadata upload(UploadFileCommand uploadFileCommand);
    FileResource find(String fileGuid);
    void delete(String fileId);
}
