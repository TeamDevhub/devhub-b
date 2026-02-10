package teamdevhub.devhub.small.core.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.file.application.FileService;
import teamdevhub.devhub.core.file.application.StoredFile;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.fake.pure.application.port.out.file.FakeFileMetadataRepository;
import teamdevhub.devhub.fake.pure.application.port.out.file.FakeFileStorage;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FileServiceTest {

    private FileService fileService;

    private FakeFileStorage fileStorage;
    private FakeFileMetadataRepository fileMetadataRepository;

    @BeforeEach
    void init() {
        FakeUuidIdentifierProvider identifierProvider = new FakeUuidIdentifierProvider("TestFileGuid1");
        fileStorage = new FakeFileStorage();
        fileMetadataRepository = new FakeFileMetadataRepository();

        fileService = new FileService(identifierProvider, fileStorage, fileMetadataRepository);
    }

    @Test
    @DisplayName("파일_업로드_테스트")
    void upload_with_fake() {
        // given
        UploadFileCommand command = new UploadFileCommand(
                "test.txt",
                "txt",
                4L,
                new ByteArrayInputStream("data".getBytes())
        );

        // when
        StoredFile storedFile = fileService.upload(command);

        // then
        assertThat(storedFile.fileGuid()).isEqualTo("TestFileGuid1");
        assertThat(fileMetadataRepository.findByFileGuid("TestFileGuid1")).isPresent();
        assertThat(fileStorage.exists("TestFileGuid1")).isTrue();
    }

    @Test
    @DisplayName("파일_삭제_테스트")
    void delete_with_fake() {
        // given
        UploadFileCommand command = new UploadFileCommand(
                "test.txt",
                "txt",
                4L,
                new ByteArrayInputStream("data".getBytes())
        );

        fileService.upload(command);

        // when
        fileService.delete("TestFileGuid1");

        // then
        assertThat(fileMetadataRepository.findByFileGuid("TestFileGuid1")).isEmpty();
        assertThat(fileStorage.exists("TestFileGuid1")).isFalse();
    }
}
