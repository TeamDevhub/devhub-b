package teamdevhub.devhub.core.file.port.in.command;

import org.springframework.web.multipart.MultipartFile;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.io.IOException;

public record UploadFileCommand(
        String originalName,
        String extension,
        long size,
        byte[] content
) {

    public static UploadFileCommand from(MultipartFile multipartFile) {
        try {

            if (multipartFile == null || multipartFile.isEmpty()) {
                throw BusinessRuleException.of(ErrorCode.FILE_EMPTY);
            }

            String originalName = multipartFile.getOriginalFilename();
            String extension = extractExtension(originalName);

            return new UploadFileCommand(
                    originalName,
                    extension,
                    multipartFile.getSize(),
                    multipartFile.getBytes()
            );

        } catch (IOException e) {
            throw BusinessRuleException.of(ErrorCode.FILE_READ_FAIL);
        }
    }

    private static String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
