package ru.hogwarts.school;

//import org.assertj.core.api.Assertions;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AvatarControllerTests {
@LocalServerPort
private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private StudentRepository studentRepository;

   // @MockitoBean
    @Autowired
    private AvatarService avatarService;
    private Avatar avatar;
    private Student student, savedStudent;

    @Test

    void testUploadAvatar() {
        // Arrange: создаём и сохраняем студента
        Student student = new Student();
        student.setName("TestStudent");
        student.setAge(20);
        student = studentRepository.saveAndFlush(student);

        // файл для загрузки
        byte[] fileContent = {1, 2, 3, 4, 5};
        Resource avatarResource = new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return "avatar.jpg";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("avatar", avatarResource);

                HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/" + student.getId() + "/avatar",
                requestEntity,
                String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        avatarRepository.deleteAll();
//        studentRepository.deleteAll();
    }
    @Test
    void testDownloadAvatar() {
        // 1. Сохраняем студента
        Student student = new Student();
        student.setName("TestStudent");
        student.setAge(20);
        student = studentRepository.saveAndFlush(student); // flush для немедленного сохранения и получения id

        // 2. Создаём и сохраняем аватар
        Avatar avatar = new Avatar();
        avatar.setStudent(student); // student уже managed и с id
        avatar.setMediaType("image/jpeg");
        avatar.setData(new byte[]{1, 2, 3, 4, 5});
        avatar.setFileSize(5L);
        avatar = avatarRepository.saveAndFlush(avatar); // id аватара = id студента

        // 3. (Не обязательно) можно связать обратно, но не сохранять student!
        // student.setAvatar(avatar);

        // 4. Делаем GET-запрос
        ResponseEntity<byte[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/" + avatar.getId() + "/avatar-from-db",
                byte[].class
        );

        // 5. Проверяем результат
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.valueOf("image/jpeg"));
        assertThat(response.getHeaders().getContentLength()).isEqualTo(5L);
        assertThat(response.getBody()).containsExactly(1, 2, 3, 4, 5);
    }

}