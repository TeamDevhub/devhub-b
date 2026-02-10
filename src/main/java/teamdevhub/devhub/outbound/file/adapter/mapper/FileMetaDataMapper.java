package teamdevhub.devhub.outbound.file.adapter.mapper;

import teamdevhub.devhub.core.file.application.StoredFile;
import teamdevhub.devhub.outbound.file.adapter.entity.FileEntity;

public class FileMetaDataMapper {

    public static FileEntity toEntity(StoredFile storedFile) {
        return FileEntity.builder()
                .fileGuid(storedFile.fileGuid())
                .originalName(storedFile.originalName())
                .extensionName(storedFile.extensionName())
                .size((int) storedFile.size())
                .path(storedFile.path())
                .build();
    }

    public static StoredFile toStoredFile(FileEntity fileEntity) {
        return new StoredFile(
                fileEntity.getFileGuid(),
                fileEntity.getOriginalName(),
                fileEntity.getExtensionName(),
                fileEntity.getSize(),
                fileEntity.getPath()
        );
    }
}