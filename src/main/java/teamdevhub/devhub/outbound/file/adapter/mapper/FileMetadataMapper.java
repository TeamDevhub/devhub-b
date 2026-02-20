package teamdevhub.devhub.outbound.file.adapter.mapper;

import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.outbound.file.adapter.entity.FileEntity;

public final class FileMetadataMapper {

    private FileMetadataMapper() {
    }

    public static FileEntity toEntity(FileMetadata metadata) {
        return FileEntity.builder()
                .fileGuid(metadata.fileGuid())
                .originalName(metadata.originalName())
                .extensionName(metadata.extensionName())
                .path(metadata.path())
                .size(metadata.size())
                .build();
    }

    public static FileMetadata toDomain(FileEntity entity) {
        return FileMetadata.create(
                entity.getFileGuid(),
                entity.getOriginalName(),
                entity.getExtensionName(),
                entity.getPath(),
                entity.getSize()
        );
    }
}