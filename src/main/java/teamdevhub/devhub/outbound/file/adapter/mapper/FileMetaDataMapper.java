package teamdevhub.devhub.outbound.file.adapter.mapper;

import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.outbound.file.adapter.entity.FileEntity;

public class FileMetaDataMapper {

    public static FileEntity toEntity(FileMetadata fileMetadata) {
        return FileEntity.builder()
                .fileGuid(fileMetadata.fileGuid())
                .originalName(fileMetadata.originalName())
                .extensionName(fileMetadata.extensionName())
                .size((int) fileMetadata.size())
                .path(fileMetadata.path())
                .build();
    }

    public static FileMetadata toStoredFile(FileEntity fileEntity) {
        return new FileMetadata(
                fileEntity.getFileGuid(),
                fileEntity.getOriginalName(),
                fileEntity.getExtensionName(),
                fileEntity.getSize(),
                fileEntity.getPath()
        );
    }
}