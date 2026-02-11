package teamdevhub.devhub.core.file.port.in.usecase;

import teamdevhub.devhub.core.file.application.FileResource;
import teamdevhub.devhub.core.file.application.StoredFile;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;

public interface FileUseCase {

    StoredFile upload(UploadFileCommand uploadFileCommand);
    FileResource find(String fileGuid);
    void delete(String fileId);
}
