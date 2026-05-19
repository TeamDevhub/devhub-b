package teamdevhub.devhub.small.api.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.api.file.controller.FileResponseFactory;
import teamdevhub.devhub.core.file.application.FileMetadata;
import teamdevhub.devhub.core.file.application.FileResource;

import static org.assertj.core.api.Assertions.assertThat;

class FileResponseFactoryTest {

    private FileResource pngResource() {
        FileMetadata metadata = FileMetadata.create("guid1", "image.png", "png", "/files/guid1", 10L);
        return FileResource.of(metadata, new byte[]{1, 2, 3});
    }

    private FileResource textResource() {
        FileMetadata metadata = FileMetadata.create("guid2", "doc.txt", "txt", "/files/guid2", 10L);
        return FileResource.of(metadata, new byte[]{4, 5, 6});
    }

    @Test
    @DisplayName("inline_응답은_200_상태코드와_파일_내용을_반환한다")
    void inline_returnsWith200AndContent() {
        // given
        FileResource resource = pngResource();

        // when
        ResponseEntity<byte[]> response = FileResponseFactory.inline(resource);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new byte[]{1, 2, 3});
        assertThat(response.getHeaders().getContentType()).isNotNull();
        assertThat(response.getHeaders().getContentType().toString()).isEqualTo("image/png");
    }

    @Test
    @DisplayName("attachment_응답은_Content-Disposition_헤더에_filename이_포함된다")
    void attachment_includesContentDispositionHeader() {
        // given
        FileResource resource = pngResource();

        // when
        ResponseEntity<byte[]> response = FileResponseFactory.attachment(resource);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new byte[]{1, 2, 3});

        String contentDisposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        assertThat(contentDisposition).isNotNull();
        assertThat(contentDisposition).contains("attachment");
        assertThat(contentDisposition).contains("filename=");
        assertThat(contentDisposition).contains("image.png");
    }

    @Test
    @DisplayName("attachment_응답의_Content-Disposition에_특수문자가_있으면_언더스코어로_대체된다")
    void attachment_sanitizesSpecialCharsInFilename() {
        // given
        FileMetadata metadata = FileMetadata.create("guid3", "file\r\nname.txt", "txt", "/files/guid3", 10L);
        FileResource resource = FileResource.of(metadata, new byte[]{1});

        // when
        ResponseEntity<byte[]> response = FileResponseFactory.attachment(resource);

        // then
        String contentDisposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        assertThat(contentDisposition).isNotNull();
        assertThat(contentDisposition).doesNotContain("\r");
        assertThat(contentDisposition).doesNotContain("\n");
    }

    @Test
    @DisplayName("txt_파일의_inline_응답_ContentType은_application/octet-stream이다")
    void inline_txtFile_returnsOctetStream() {
        // given
        FileResource resource = textResource();

        // when
        ResponseEntity<byte[]> response = FileResponseFactory.inline(resource);

        // then
        assertThat(response.getHeaders().getContentType()).isNotNull();
        assertThat(response.getHeaders().getContentType().toString()).isEqualTo("application/octet-stream");
    }
}
