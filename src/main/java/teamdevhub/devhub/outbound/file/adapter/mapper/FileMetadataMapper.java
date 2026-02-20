package teamdevhub.devhub.outbound.file.adapter.mapper;

import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.outbound.file.adapter.entity.FileEntity;

public final class FileMetadataMapper {

    private FileMetadataMapper() {
    }

    public static FileEntity toEntity(FileMetadata fileMetadata) {
        return FileEntity.builder()
                .fileGuid(fileMetadata.fileGuid())
                .originalName(fileMetadata.originalName())
                .extensionName(fileMetadata.extensionName())
                .path(fileMetadata.path())
                .size(fileMetadata.size())
                .build();
    }

    public static FileMetadata toDomain(FileEntity fileEntity) {
        return FileMetadata.create(
                fileEntity.getFileGuid(),
                fileEntity.getOriginalName(),
                fileEntity.getExtensionName(),
                fileEntity.getPath(),
                fileEntity.getSize()
        );
    }
}