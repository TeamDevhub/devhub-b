package teamdevhub.devhub.medium.outbound.file.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.outbound.file.adapter.FileMetadataAdapter;
import teamdevhub.devhub.outbound.file.persistence.JpaFileRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class FileMetadataAdapterTest {

    @Autowired
    private FileMetadataAdapter fileMetadataAdapter;

    @Autowired
    private JpaFileRepository jpaFileRepository;

    @BeforeEach
    void init() {
        jpaFileRepository.deleteAll();
    }

    private FileMetadata sampleMetadata(String guid) {
        return FileMetadata.create(guid, "sample.png", "png", "/files/" + guid, 512L);
    }

    @Test
    @DisplayName("파일_메타데이터를_저장하면_DB에_저장된다")
    void save_persistsFileMetadata() {
        // given
        FileMetadata metadata = sampleMetadata("FILE_GUID_001");

        // when
        FileMetadata saved = fileMetadataAdapter.save(metadata);

        // then
        assertThat(saved.fileGuid()).isEqualTo("FILE_GUID_001");
        assertThat(saved.originalName()).isEqualTo("sample.png");
        assertThat(jpaFileRepository.findByFileGuid("FILE_GUID_001")).isPresent();
    }

    @Test
    @DisplayName("파일_GUID로_메타데이터를_조회하면_저장된_정보를_반환한다")
    void find_existingGuid_returnsFileMetadata() {
        // given
        fileMetadataAdapter.save(sampleMetadata("FILE_GUID_002"));

        // when
        FileMetadata result = fileMetadataAdapter.find("FILE_GUID_002");

        // then
        assertThat(result.fileGuid()).isEqualTo("FILE_GUID_002");
        assertThat(result.originalName()).isEqualTo("sample.png");
        assertThat(result.extensionName()).isEqualTo("png");
        assertThat(result.size()).isEqualTo(512L);
    }

    @Test
    @DisplayName("존재하지_않는_GUID로_조회하면_AdapterDataException이_발생한다")
    void find_nonExistentGuid_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> fileMetadataAdapter.find("NOT_EXIST_GUID"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.FILE_READ_FAIL.getMessage());
    }

    @Test
    @DisplayName("파일_GUID로_삭제하면_DB에서_제거된다")
    void deleteByFileGuid_removesFromDb() {
        // given
        fileMetadataAdapter.save(sampleMetadata("FILE_GUID_003"));
        assertThat(jpaFileRepository.findByFileGuid("FILE_GUID_003")).isPresent();

        // when
        fileMetadataAdapter.deleteByFileGuid("FILE_GUID_003");

        // then
        assertThat(jpaFileRepository.findByFileGuid("FILE_GUID_003")).isEmpty();
    }

    @Test
    @DisplayName("저장된_파일의_path_정보가_올바르게_유지된다")
    void save_pathIsPreserved() {
        // given
        FileMetadata metadata = FileMetadata.create("FILE_GUID_004", "doc.pdf", "pdf", "/uploads/docs/FILE_GUID_004", 2048L);

        // when
        fileMetadataAdapter.save(metadata);
        FileMetadata found = fileMetadataAdapter.find("FILE_GUID_004");

        // then
        assertThat(found.path()).isEqualTo("/uploads/docs/FILE_GUID_004");
    }
}
