package teamdevhub.devhub.small.core.file.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.core.file.application.FileResource;
import teamdevhub.devhub.core.file.application.FileService;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.core.file.port.in.facade.model.FileResponseDto;
import teamdevhub.devhub.fake.pure.application.port.out.file.FakeFileMetadataRepository;
import teamdevhub.devhub.fake.pure.application.port.out.file.FakeFileStorage;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.shared.enums.ErrorCode;

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

    private UploadFileCommand textFileCommand() {
        return new UploadFileCommand("test.txt", "txt", 4L, "data".getBytes());
    }

    @Test
    @DisplayName("파일_업로드_테스트")
    void upload_with_fake() {
        // when
        FileMetadata metadata = fileService.upload(textFileCommand());

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
        fileService.upload(textFileCommand());

        // when
        FileResource resource = fileService.find("TestFileGuid1");

        // then
        assertThat(resource.metadata().fileGuid()).isEqualTo("TestFileGuid1");
        assertThat(resource.content()).isEqualTo("data".getBytes());
    }

    @Test
    @DisplayName("존재하지_않는_파일을_조회하면_AdapterDataException이_발생한다")
    void find_nonExistent_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> fileService.find("nonExistentGuid"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }

    @Test
    @DisplayName("파일_삭제_후_조회하면_AdapterDataException이_발생한다")
    void delete_thenFind_throwsAdapterDataException() {
        // given
        fileService.upload(textFileCommand());

        // when
        fileService.delete("TestFileGuid1");

        // then
        assertThatThrownBy(() -> fileService.find("TestFileGuid1"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());

        assertThat(fileStorage.exists("TestFileGuid1")).isFalse();
    }

    @Test
    @DisplayName("파일_메타데이터를_조회하면_파일정보를_반환한다")
    void selectFileObject_returnsFileResponseDto() {
        // given
        fileService.upload(textFileCommand());

        // when
        FileResponseDto result = fileService.selectFileObject("TestFileGuid1");

        // then
        assertThat(result.fileGuid()).isEqualTo("TestFileGuid1");
        assertThat(result.filename()).isEqualTo("test.txt");
        assertThat(result.size()).isEqualTo(4L);
    }

    @Test
    @DisplayName("존재하지_않는_파일의_메타데이터_조회시_AdapterDataException이_발생한다")
    void selectFileObject_nonExistent_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> fileService.selectFileObject("nonExistentGuid"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }
}
