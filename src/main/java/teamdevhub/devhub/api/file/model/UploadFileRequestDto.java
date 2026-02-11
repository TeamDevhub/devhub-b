package teamdevhub.devhub.api.file.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public record UploadFileRequestDto(Map<String, MultipartFile> files) {

    public static UploadFileRequestDto from(Map<String, MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("No files provided");
        }
        return new UploadFileRequestDto(files);
    }
}