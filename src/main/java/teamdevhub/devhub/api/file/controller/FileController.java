package teamdevhub.devhub.api.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamdevhub.devhub.api.file.model.UploadFileRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.core.file.port.in.facade.FileFacade;
import teamdevhub.devhub.core.file.port.in.facade.model.FileResponseDto;
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
