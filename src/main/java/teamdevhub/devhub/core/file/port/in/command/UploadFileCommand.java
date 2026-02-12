package teamdevhub.devhub.core.file.port.in.command;

import org.springframework.web.multipart.MultipartFile;

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
                throw new IllegalArgumentException("Empty file");
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
            throw new RuntimeException("Failed to read multipart file", e);
        }
    }

    private static String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
