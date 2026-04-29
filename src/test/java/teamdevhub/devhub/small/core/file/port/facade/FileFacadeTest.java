package teamdevhub.devhub.small.core.file.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.file.application.FileResource;
import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
import teamdevhub.devhub.core.file.port.in.facade.FileFacade;
import teamdevhub.devhub.core.file.port.in.facade.model.UploadFileResponseDto;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.file.FakeFileUseCase;

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

    @Test
    @DisplayName("파일_다중_업로드_테스트")
    void upload_multiple_files() {
        // given
        UploadFileCommand command1 =
                new UploadFileCommand("a.txt", "txt", 4L, "data".getBytes());

        UploadFileCommand command2 =
                new UploadFileCommand("b.txt", "txt", 4L, "data".getBytes());

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
        UploadFileCommand uploadFileCommand =
                new UploadFileCommand("a.txt", "txt", 4L, "data".getBytes());

        String guid = fileFacade.upload(Map.of("file", uploadFileCommand))
                .fileGuids()
                .get("file");

        // when
        FileResource resource = fileFacade.find(guid);

        // then
        assertThat(resource.metadata().fileGuid()).isEqualTo(guid);
        assertThat(resource.content()).isEqualTo("data".getBytes());
    }

    @Test
    @DisplayName("파일_삭제_테스트")
    void delete_test() {
        UploadFileCommand uploadFileCommand =
                new UploadFileCommand("a.txt", "txt", 4L, "data".getBytes());

        String guid = fileFacade.upload(Map.of("file", uploadFileCommand))
                .fileGuids()
                .get("file");

        // when
        fileFacade.delete(guid);

        // then
        assertThatThrownBy(() -> fileFacade.find(guid))
                .isInstanceOf(RuntimeException.class);
    }
}
