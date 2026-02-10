package teamdevhub.devhub.core.file.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.core.file.application.StoredFile;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.core.file.port.in.facade.model.FileResponseDto;
import teamdevhub.devhub.core.file.port.in.usecase.FileUseCase;

@Service
@RequiredArgsConstructor
public class FileFacade {

    private final FileUseCase fileUseCase;

    public FileResponseDto upload(UploadFileCommand uploadFileCommand) {
        StoredFile storedFile = fileUseCase.upload(uploadFileCommand);
        return FileResponseDto.from(storedFile);
    }

    public void delete(String fileGuid) {
        fileUseCase.delete(fileGuid);
    }
}
