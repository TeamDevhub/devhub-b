package teamdevhub.devhub.api.file.model;

import org.springframework.web.multipart.MultipartFile;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public record UploadFileRequestDto(
        Map<String, MultipartFile> files
) {

    public static UploadFileRequestDto from(Map<String, MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("No files provided");
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
            throw new RuntimeException("파일 변환 실패", e);
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}