//package teamdevhub.devhub.small.core.file;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import teamdevhub.devhub.core.file.application.FileService;
//import teamdevhub.devhub.core.file.application.StoredFile;
//import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
//import teamdevhub.devhub.fake.pure.application.port.out.file.FakeFileMetadataRepository;
//import teamdevhub.devhub.fake.pure.application.port.out.file.FakeFileStorage;
//import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
//
//import java.io.FileNotFoundException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//public class FileServiceTest {
//
//    private FileService fileService;
//
//    private FakeFileStorage fileStorage;
//    private FakeFileMetadataRepository fileMetadataRepository;
//
//    @BeforeEach
//    void init() {
//        FakeUuidIdentifierProvider identifierProvider = new FakeUuidIdentifierProvider("TestFileGuid1");
//        fileStorage = new FakeFileStorage();
//        fileMetadataRepository = new FakeFileMetadataRepository();
//
//        fileService = new FileService(identifierProvider, fileStorage, fileMetadataRepository);
//    }
//
//    @Test
//    @DisplayName("파일_업로드_테스트")
//    void upload_with_fake() {
//        // given
//        byte[] content = "data".getBytes();
//
//        UploadFileCommand command = new UploadFileCommand(
//                "test.txt",
//                "txt",
//                4L,
//                content
//        );
//
//        // when
//        StoredFile storedFile = fileService.upload(command);
//
//        // then
//        assertThat(storedFile.fileGuid()).isEqualTo("TestFileGuid1");
//
//        StoredFile found = fileMetadataRepository.find("TestFileGuid1");
//        assertThat(found).isNotNull();
//
//        assertThat(fileStorage.exists("TestFileGuid1")).isTrue();
//    }
//
//    @Test
//    @DisplayName("파일_삭제_테스트")
//    void delete_with_fake() {
//        byte[] content = "data".getBytes();
//
//        UploadFileCommand command = new UploadFileCommand(
//                "test.txt",
//                "txt",
//                4L,
//                content
//        );
//
//        StoredFile storedFile = fileService.upload(command);
//
//        fileService.delete(storedFile.fileGuid());
//
//        assertThatThrownBy(() ->
//                fileService.find(storedFile.fileGuid())
//        ).isInstanceOf(FileNotFoundException.class);
//
//        assertThat(fileStorage.exists(storedFile.fileGuid())).isFalse();
//    }
//}
