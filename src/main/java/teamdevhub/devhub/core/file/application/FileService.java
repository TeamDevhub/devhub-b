package teamdevhub.devhub.core.file.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
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
    public StoredFile upload(UploadFileCommand uploadFileCommand) {
        String fileGuid = identifierProvider.generateIdentifier();
        String savedPath = fileStorage.save(fileGuid, uploadFileCommand.content());
        StoredFile storedFile = StoredFile.from(fileGuid, uploadFileCommand, savedPath);
        return fileMetadataRepository.save(storedFile);
    }

    @Override
    public void delete(String fileGuid) {
        fileStorage.delete(fileGuid);
        fileMetadataRepository.deleteByFileGuid(fileGuid);
    }
}
