package teamdevhub.devhub.core.file.port.in.usecase;

import teamdevhub.devhub.core.file.application.FileResource;
import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.core.file.port.in.facade.model.FileResponseDto;

public interface FileUseCase {

    FileMetadata upload(UploadFileCommand uploadFileCommand);
    FileResource find(String fileGuid);
    void delete(String fileId);

    FileResponseDto selectFileObject(String fileGuid);
}
