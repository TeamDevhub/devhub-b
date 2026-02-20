package teamdevhub.devhub.api.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamdevhub.devhub.api.file.model.UploadFileRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
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
    public ResponseEntity<DataApiResponseDto<UploadFileResponseDto>> upload(@RequestParam Map<String, MultipartFile> uploadFiles) {
        UploadFileRequestDto uploadFileRequestDto = UploadFileRequestDto.from(uploadFiles);
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.CREATE_SUCCESS,
                        fileFacade.upload(uploadFileRequestDto.toCommandMap())
                )
        );
    }

    @GetMapping("/{fileGuid}")
    public ResponseEntity<byte[]> view(@PathVariable String fileGuid) {
        return FileResponseFactory.inline(fileFacade.find(fileGuid));
    }

    @GetMapping("/{fileGuid}/download")
    public ResponseEntity<byte[]> download(@PathVariable String fileGuid) {
        return FileResponseFactory.attachment(fileFacade.find(fileGuid));
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
}
