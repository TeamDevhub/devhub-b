package teamdevhub.devhub.fake.pure.application.port.in.usecase.file;

import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.core.file.application.FileResource;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.core.file.port.in.facade.model.FileResponseDto;
import teamdevhub.devhub.core.file.port.in.usecase.FileUseCase;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.util.HashMap;
import java.util.Map;

public class FakeFileUseCase implements FileUseCase {

    private final Map<String, FileMetadata> metadataStore = new HashMap<>();
    private final Map<String, byte[]> contentStore = new HashMap<>();

    private int sequence = 1;

    @Override
    public FileMetadata upload(UploadFileCommand command) {
        String guid = "fake-file-" + sequence++;

        FileMetadata metadata = FileMetadata.create(
                guid,
                command.originalName(),
                command.extension(),
                "/fake/path/" + guid,
                command.size()
        );

        metadataStore.put(guid, metadata);
        contentStore.put(guid, command.content());

        return metadata;
    }

    @Override
    public FileResource find(String fileGuid) {
        FileMetadata metadata = metadataStore.get(fileGuid);
        if (metadata == null) {
            throw AdapterDataException.of(ErrorCode.FILE_READ_FAIL);
        }
        return FileResource.of(metadata, contentStore.get(fileGuid));
    }

    @Override
    public void delete(String fileGuid) {
        metadataStore.remove(fileGuid);
        contentStore.remove(fileGuid);
    }

    @Override
    public FileResponseDto selectFileObject(String fileGuid) {
        FileMetadata metadata = metadataStore.get(fileGuid);
        if (metadata == null) {
            throw AdapterDataException.of(ErrorCode.FILE_READ_FAIL);
        }
        return FileResponseDto.from(metadata);
    }
}
