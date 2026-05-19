package teamdevhub.devhub.core.file.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.core.file.port.in.facade.model.FileResponseDto;
import teamdevhub.devhub.core.file.port.in.usecase.FileUseCase;
import teamdevhub.devhub.core.file.port.out.FileMetadataRepository;
import teamdevhub.devhub.core.file.port.out.FileStorage;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService implements FileUseCase {

    private final IdentifierProvider identifierProvider;
    private final FileStorage fileStorage;
    private final FileMetadataRepository fileMetadataRepository;

    @Override
    public FileMetadata upload(UploadFileCommand uploadFileCommand) {
        String fileGuid = identifierProvider.generateIdentifier();
        String path = fileStorage.save(fileGuid, uploadFileCommand.content());
        FileMetadata fileMetadata = FileMetadata.create(
                fileGuid,
                uploadFileCommand.originalName(),
                uploadFileCommand.extension(),
                path,
                uploadFileCommand.size()
        );

        return fileMetadataRepository.save(fileMetadata);
    }

    @Override
    public FileResource find(String fileGuid) {
        FileMetadata fileMetadata = fileMetadataRepository.find(fileGuid);
        byte[] content = fileStorage.read(fileGuid);
        return FileResource.of(fileMetadata, content);
    }

    @Override
    public void delete(String fileGuid) {
        fileMetadataRepository.deleteByFileGuid(fileGuid);
        fileStorage.delete(fileGuid);
    }

    @Override
    public FileResponseDto selectFileObject(String fileGuid) {
        FileMetadata fileMetadata = fileMetadataRepository.find(fileGuid);
        return FileResponseDto.from(fileMetadata);
    }
}
