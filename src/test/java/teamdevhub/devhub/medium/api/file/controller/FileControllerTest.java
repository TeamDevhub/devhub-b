//package teamdevhub.devhub.medium.api.file.controller;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import teamdevhub.devhub.api.file.controller.FileController;
//import teamdevhub.devhub.core.file.port.in.command.UploadFileCommand;
//import teamdevhub.devhub.core.file.port.in.facade.FileFacade;
//import teamdevhub.devhub.core.file.port.in.facade.model.FileResponseDto;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.willDoNothing;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(FileController.class)
//class FileControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockitoBean
//    FileFacade fileFacade;
//
//    @Test
//    @DisplayName("파일 업로드 API - multipart")
//    void upload_file_success() throws Exception {
//        // given
//        MockMultipartFile multipartFile =
//                new MockMultipartFile(
//                        "multipartFile",
//                        "test.png",
//                        "image/png",
//                        "fake-image-content".getBytes()
//                );
//
//        FileResponseDto responseDto = new FileResponseDto(
//                "FILE_GUID",
//                "test.png",
//                123L,
//                "/files/FILE_GUID"
//        );
//
//        given(fileFacade.upload(any(UploadFileCommand.class)))
//                .willReturn(responseDto);
//
//        // when & then
//        mockMvc.perform(
//                        multipart("/files")
//                                .file(multipartFile)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.fileGuid").value("FILE_GUID"))
//                .andExpect(jsonPath("$.data.filename").value("test.png"))
//                .andExpect(jsonPath("$.data.downloadUrl").value("/files/FILE_GUID"));
//
//        verify(fileFacade).upload(any(UploadFileCommand.class));
//    }
//
//    @Test
//    @DisplayName("파일 삭제 API")
//    void delete_file_success() throws Exception {
//        // given
//        String fileGuid = "FILE_GUID";
//
//        willDoNothing()
//                .given(fileFacade)
//                .delete(fileGuid);
//
//        // when & then
//        mockMvc.perform(
//                        delete("/files/{fileGuid}", fileGuid)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true));
//
//        verify(fileFacade).delete(fileGuid);
//    }
//}
