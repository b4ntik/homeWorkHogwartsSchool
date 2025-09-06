package ru.hogwarts.school;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

@WebMvcTest(AvatarController.class)
class AvatarControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AvatarService avatarService;

    // загрузка аватара
    @Test
    public void testUploadAvatar() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "avatar",
                "avatar.png",
                MediaType.IMAGE_PNG_VALUE,
                "dummy image content".getBytes()
        );

        mockMvc.perform(multipart("/{studentId}/avatar", 1L)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        // проверка, что сервис вызвался с нужными параметрами
        verify(avatarService).uploadAvatar(eq(1L), any());
    }

    // поиск аватара
    @Test
    public void testDownloadAvatarFromDb() throws Exception {
        byte[] data = "image data".getBytes();
        Avatar avatar = new Avatar();
        avatar.setMediaType(MediaType.IMAGE_JPEG_VALUE);
        avatar.setData(data);

        when(avatarService.findAvatar(1L)).thenReturn(avatar);

        mockMvc.perform(get("/{id}/avatar-from-db", 1L))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.IMAGE_JPEG_VALUE))
                .andExpect(header().longValue("Content-Length", data.length))
                .andExpect(content().bytes(data));
    }

    // загрузка аватара из файла
    @Test
    public void testDownloadAvatarFromFile() throws Exception {
        byte[] fileContent = "file content".getBytes();
        Avatar avatar = new Avatar();
        avatar.setMediaType(MediaType.IMAGE_PNG_VALUE);
        avatar.setFilePath("test-file-path");
        avatar.setFileSize(fileContent.length);

        when(avatarService.findAvatar(1L)).thenReturn(avatar);

        // мок Files.newInputStream(Path) через spy Path и используем временный файл
        Path tempFile = Files.createTempFile("avatar", ".tmp");
        Files.write(tempFile, fileContent);

        // переопределить getFilePath чтобы вернуть tempFile.toString()
        avatar.setFilePath(tempFile.toString());

        MvcResult result = mockMvc.perform(get("/{id}/avatar-from-file", 1L))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.IMAGE_PNG_VALUE))
                .andExpect(header().longValue("Content-Length", fileContent.length))
                .andReturn();

        // проверка тела ответа
        byte[] responseBytes = result.getResponse().getContentAsByteArray();
        org.assertj.core.api.Assertions.assertThat(responseBytes).isEqualTo(fileContent);

        Files.deleteIfExists(tempFile);
    }

    // изменить аватар
    @Test
    public void testChangeAvatar() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "avatar",
                "avatar2.png",
                MediaType.IMAGE_PNG_VALUE,
                "new avatar content".getBytes()
        );

        mockMvc.perform(multipart("/{studentId}/avatar", 2L)
                        .file(file)
                        .with(request -> { request.setMethod("PUT"); return request; }) // multipart по умолчанию POST, меняем на PUT
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(avatarService).uploadAvatar(eq(2L), any());
    }

    // удаление аватара
    @Test
    public void testDeleteAvatar() throws Exception {
        mockMvc.perform(delete("/avatar")
                        .param("id", "3"))
                .andExpect(status().isOk());

        verify(avatarService).deleteAvatarByStudentId(3L);
    }
}