package teamdevhub.devhub.small.core.file.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.core.file.application.FileResource;
import teamdevhub.devhub.core.file.application.FileService;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.fake.pure.application.port.out.file.FakeFileMetadataRepository;
import teamdevhub.devhub.fake.pure.application.port.out.file.FakeFileStorage;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileServiceTest {

    private FileService fileService;

    private FakeFileStorage fileStorage;
    private FakeFileMetadataRepository fileMetadataRepository;

    @BeforeEach
    void init() {
        FakeUuidIdentifierProvider identifierProvider =
                new FakeUuidIdentifierProvider("TestFileGuid1");

        fileStorage = new FakeFileStorage();
        fileMetadataRepository = new FakeFileMetadataRepository();

        fileService = new FileService(
                identifierProvider,
                fileStorage,
                fileMetadataRepository
        );
    }

    @Test
    @DisplayName("파일_업로드_테스트")
    void upload_with_fake() {
        // given
        byte[] content = "data".getBytes();

        UploadFileCommand uploadFileCommand = new UploadFileCommand(
                "test.txt",
                "txt",
                4L,
                content
        );

        // when
        FileMetadata metadata = fileService.upload(uploadFileCommand);

        // then
        assertThat(metadata.fileGuid()).isEqualTo("TestFileGuid1");
        assertThat(metadata.originalName()).isEqualTo("test.txt");
        assertThat(metadata.extensionName()).isEqualTo("txt");
        assertThat(metadata.size()).isEqualTo(4L);

        assertThat(fileStorage.exists("TestFileGuid1")).isTrue();
        assertThat(fileMetadataRepository.find("TestFileGuid1")).isNotNull();
    }

    @Test
    @DisplayName("파일_조회_테스트")
    void find_with_fake() {
        // given
        byte[] content = "data".getBytes();

        UploadFileCommand uploadFileCommand = new UploadFileCommand(
                "test.txt",
                "txt",
                4L,
                content
        );

        fileService.upload(uploadFileCommand);

        // when
        FileResource resource = fileService.find("TestFileGuid1");

        // then
        assertThat(resource.metadata().fileGuid()).isEqualTo("TestFileGuid1");
        assertThat(resource.content()).isEqualTo(content);
    }

    @Test
    @DisplayName("파일_삭제_테스트")
    void delete_with_fake() {
        // given
        byte[] content = "data".getBytes();

        UploadFileCommand uploadFileCommand = new UploadFileCommand(
                "test.txt",
                "txt",
                4L,
                content
        );

        fileService.upload(uploadFileCommand);

        // when
        fileService.delete("TestFileGuid1");

        // then
        assertThatThrownBy(() ->
                fileService.find("TestFileGuid1")
        ).isInstanceOf(RuntimeException.class);

        assertThat(fileStorage.exists("TestFileGuid1")).isFalse();
    }
}