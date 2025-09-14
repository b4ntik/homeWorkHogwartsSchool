package ru.hogwarts.school;


import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AvatarControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent;


    @Test
    void testUploadAvatar() throws IOException {
        testStudent = new Student();
        testStudent.setName("TestStudent");
        testStudent.setAge(20);
        testStudent = studentRepository.save(testStudent);
        // создаём файл для загрузки
        byte[] fileContent = {1, 2, 3, 4, 5};
        Resource resource = new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return "avatar.jpg";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("avatar", resource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String url = "http://localhost:" + port + "/avatar/" + testStudent.getId();

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void testDownloadAvatarFromDb() {
        // предварительно создаём аватар в базе
        testStudent = new Student();
        testStudent.setName("TestStudent");
        testStudent.setAge(20);
        //testStudent = studentRepository.save(testStudent);
        Avatar avatar = new Avatar();

        avatar.setMediaType("image/jpeg");
        avatar.setData(new byte[]{1, 2, 3});
        avatar.setFileSize(3L);
        avatar.setStudent(testStudent);
        avatar.setId(testStudent.getId());
        avatarRepository.save(avatar);

        String url = "http://localhost:" + port + "/avatar-from-db/" + avatar.getId();

        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.parseMediaType("image/jpeg"), response.getHeaders().getContentType());
        Assertions.assertArrayEquals(new byte[]{1, 2, 3}, response.getBody());
        avatarRepository.deleteAll();
       studentRepository.deleteAll();
    }

    @Test
    void testDownloadAvatarFromFile() throws IOException {
        // создаем файл и его путь
        Path tempFile = Files.createTempFile("avatar", ".jpg");
        Files.write(tempFile, new byte[]{4, 5, 6});
        // создаем аватар
        testStudent = new Student();
        testStudent.setName("TestStudent");
        testStudent.setAge(20);
        Avatar avatar = new Avatar();
        avatar.setStudent(testStudent);
        avatar.setId(testStudent.getId());
        avatar.setFilePath(tempFile.toString());
        avatar.setMediaType("image/jpeg");
        avatar.setFileSize(3L);
        avatar.setData(new byte[]{4, 5, 6});
        avatarRepository.save(avatar);

        String url = "http://localhost:" + port + "/avatar-from-file/" + avatar.getId();

        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(MediaType.parseMediaType("image/jpeg"), response.getHeaders().getContentType());
        Assertions.assertArrayEquals(new byte[]{4, 5, 6}, response.getBody());

        Files.deleteIfExists(tempFile);
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void testChangeAvatar() throws IOException {
        // cоздаём аватар
        testStudent = new Student();
        testStudent.setName("TestStudent");
        testStudent.setAge(20);
        Avatar avatar = new Avatar();
        avatar.setId(testStudent.getId());
        avatar.setStudent(testStudent);
        avatar.setMediaType("image/jpeg");
        avatar.setData(new byte[]{1});
        avatar.setFileSize(1L);
        avatarRepository.save(avatar);

        // cоздаём новый файл
        byte[] newContent = {7, 8, 9};
        Resource resource = new ByteArrayResource(newContent) {
            @Override
            public String getFilename() {
                return "new_avatar.jpg";
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("avatar", resource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String url = "http://localhost:" + port + "/avatar/" + testStudent.getId();

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test

    void testDeleteAvatar() {
        // cоздаём аватар
        testStudent = new Student();
        testStudent.setName("TestStudent");
        testStudent.setAge(20);
        testStudent = studentRepository.save(testStudent);
        Avatar avatar = new Avatar();
        avatar.setId(testStudent.getId());
        avatar.setStudent(testStudent);
        avatar.setMediaType("image/jpeg");
        avatar.setData(new byte[]{1});
        avatar.setFileSize(1L);
        //avatarRepository.save(avatar);

        String url = "http://localhost:" + port + "/avatar?id={id}";
        restTemplate.delete(url, avatar.getId());

        Optional<Avatar> deleted = avatarRepository.findById(avatar.getId());
        Assertions.assertFalse(deleted.isPresent());
    }
}