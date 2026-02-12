package teamdevhub.devhub.core.file.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.core.file.application.FileResource;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.core.file.port.in.facade.model.UploadFileResponseDto;
import teamdevhub.devhub.core.file.port.in.usecase.FileUseCase;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileFacade {

    private final FileUseCase fileUseCase;

    public UploadFileResponseDto upload(Map<String, UploadFileCommand> commands) {
        return UploadFileResponseDto.from(
                commands.entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> fileUseCase.upload(entry.getValue()).fileGuid()
                        ))
        );
    }

    public FileResource find(String fileGuid) {
        return fileUseCase.find(fileGuid);
    }

    public void delete(String fileGuid) {
        fileUseCase.delete(fileGuid);
    }
}
