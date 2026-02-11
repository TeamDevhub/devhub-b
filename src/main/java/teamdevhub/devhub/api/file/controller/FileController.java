package teamdevhub.devhub.api.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamdevhub.devhub.api.file.model.UploadFileRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.core.file.application.FileResource;
import teamdevhub.devhub.core.file.port.in.facade.FileFacade;
import teamdevhub.devhub.core.file.port.in.facade.model.UploadFileResponseDto;
import teamdevhub.devhub.shared.enums.SuccessCode;

import java.util.Map;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileFacade fileFacade;

    @PostMapping
    public ResponseEntity<DataApiResponseDto<UploadFileResponseDto>> upload(@RequestPart Map<String, MultipartFile> uploadFiles) {
        UploadFileRequestDto uploadFileRequestDto = new UploadFileRequestDto(uploadFiles);
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.CREATE_SUCCESS,
                        fileFacade.upload(uploadFileRequestDto)
                )
        );
    }

    @GetMapping("/{fileGuid}")
    public ResponseEntity<byte[]> view(@PathVariable String fileGuid) {
        FileResource fileResource = fileFacade.find(fileGuid);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(resolveContentType(fileResource.metadata().extensionName()))).body(fileResource.content());
    }

    @GetMapping("/{fileGuid}/download")
    public ResponseEntity<byte[]> download(@PathVariable String fileGuid) {

        FileResource resource = fileFacade.find(fileGuid);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.metadata().originalName() + "\"")
                .body(resource.content());
    }

    @DeleteMapping("/{fileGuid}")
    public ResponseEntity<DataApiResponseDto<Void>> delete(@PathVariable String fileGuid) {
        fileFacade.delete(fileGuid);
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.DELETE_SUCCESS
                )
        );
    }

    private String resolveContentType(String extension) {
        return switch (extension.toLowerCase()) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }
}
