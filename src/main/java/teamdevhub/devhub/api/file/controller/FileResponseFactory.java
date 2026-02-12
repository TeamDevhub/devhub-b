package teamdevhub.devhub.api.file.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.core.file.application.FileResource;

public final class FileResponseFactory {

    private FileResponseFactory() {}

    public static ResponseEntity<byte[]> inline(FileResource fileResource) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileResource.contentType()))
                .body(fileResource.content());
    }

    public static ResponseEntity<byte[]> attachment(FileResource fileResource) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileResource.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileResource.originalName() + "\"")
                .body(fileResource.content());
    }
}
