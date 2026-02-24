package teamdevhub.devhub.api.file.model;

import org.springframework.web.multipart.MultipartFile;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public record UploadFileRequestDto(
        Map<String, MultipartFile> files
) {

    public static UploadFileRequestDto from(Map<String, MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw BusinessRuleException.of(ErrorCode.FILE_EMPTY);
        }
        return new UploadFileRequestDto(files);
    }

    public Map<String, UploadFileCommand> toCommandMap() {
        return files.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> toCommand(entry.getValue())
                ));
    }

    private UploadFileCommand toCommand(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String extension = extractExtension(originalName);

            return new UploadFileCommand(
                    originalName,
                    extension,
                    file.getSize(),
                    file.getBytes()
            );

        } catch (IOException e) {
            throw BusinessRuleException.of(ErrorCode.FILE_READ_FAIL);
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}