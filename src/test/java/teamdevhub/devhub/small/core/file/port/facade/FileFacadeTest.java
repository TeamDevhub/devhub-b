package teamdevhub.devhub.small.core.file.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.core.file.application.FileResource;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.core.file.port.in.facade.FileFacade;
import teamdevhub.devhub.core.file.port.in.facade.model.FileResponseDto;
import teamdevhub.devhub.core.file.port.in.facade.model.UploadFileResponseDto;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.file.FakeFileUseCase;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileFacadeTest {

    private FileFacade fileFacade;

    @BeforeEach
    void init() {
        FakeFileUseCase fakeFileUseCase = new FakeFileUseCase();
        fileFacade = new FileFacade(fakeFileUseCase);
    }

    private String uploadSingle(String name) {
        UploadFileCommand command = new UploadFileCommand(name + ".txt", "txt", 4L, "data".getBytes());
        return fileFacade.upload(Map.of("file", command)).fileGuids().get("file");
    }

    @Test
    @DisplayName("파일_다중_업로드_테스트")
    void upload_multiple_files() {
        // given
        UploadFileCommand command1 = new UploadFileCommand("a.txt", "txt", 4L, "data".getBytes());
        UploadFileCommand command2 = new UploadFileCommand("b.txt", "txt", 4L, "data".getBytes());

        Map<String, UploadFileCommand> commands = Map.of(
                "profileImage", command1,
                "resume", command2
        );

        // when
        UploadFileResponseDto response = fileFacade.upload(commands);

        // then
        assertThat(response.fileGuids()).hasSize(2);
        assertThat(response.fileGuids()).containsKeys("profileImage", "resume");
    }

    @Test
    @DisplayName("파일_조회_테스트")
    void find_test() {
        // given
        String guid = uploadSingle("a");

        // when
        FileResource resource = fileFacade.find(guid);

        // then
        assertThat(resource.metadata().fileGuid()).isEqualTo(guid);
        assertThat(resource.content()).isEqualTo("data".getBytes());
    }

    @Test
    @DisplayName("존재하지_않는_파일을_조회하면_AdapterDataException이_발생한다")
    void find_nonExistent_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> fileFacade.find("nonExistentGuid"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }

    @Test
    @DisplayName("파일_삭제_후_조회하면_AdapterDataException이_발생한다")
    void delete_thenFind_throwsAdapterDataException() {
        // given
        String guid = uploadSingle("a");

        // when
        fileFacade.delete(guid);

        // then
        assertThatThrownBy(() -> fileFacade.find(guid))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }

    @Test
    @DisplayName("파일_메타데이터를_조회하면_파일정보를_반환한다")
    void selectFileObject_returnsFileResponseDto() {
        // given
        String guid = uploadSingle("a");

        // when
        FileResponseDto result = fileFacade.selectFileObject(guid);

        // then
        assertThat(result.fileGuid()).isEqualTo(guid);
        assertThat(result.filename()).isEqualTo("a.txt");
        assertThat(result.size()).isEqualTo(4L);
    }

    @Test
    @DisplayName("존재하지_않는_파일의_메타데이터_조회시_AdapterDataException이_발생한다")
    void selectFileObject_nonExistent_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> fileFacade.selectFileObject("nonExistentGuid"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }
}
