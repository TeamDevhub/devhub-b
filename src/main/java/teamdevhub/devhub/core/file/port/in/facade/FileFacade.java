package teamdevhub.devhub.core.file.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.api.file.model.UploadFileRequestDto;
import teamdevhub.devhub.core.file.application.FileResource;
import teamdevhub.devhub.core.file.application.StoredFile;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.core.file.port.in.facade.model.FileResponseDto;
import teamdevhub.devhub.core.file.port.in.facade.model.UploadFileResponseDto;
import teamdevhub.devhub.core.file.port.in.usecase.FileUseCase;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileFacade {

    private final FileUseCase fileUseCase;

    public UploadFileResponseDto upload(UploadFileRequestDto uploadFileRequestDto) {

        Map<String, String> result = new HashMap<>();
        uploadFileRequestDto.files().forEach((inputName, multipartFile) -> {
            UploadFileCommand command = UploadFileCommand.from(multipartFile);
            StoredFile storedFile = fileUseCase.upload(command);
            result.put(inputName, storedFile.fileGuid());
        });

        return UploadFileResponseDto.from(result);
    }

    public FileResource find(String fileGuid) {
        return fileUseCase.find(fileGuid);
    }

    public void delete(String fileGuid) {
        fileUseCase.delete(fileGuid);
    }
}
