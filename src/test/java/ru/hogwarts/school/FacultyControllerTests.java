package ru.hogwarts.school;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTests {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        facultyRepository.deleteAll();
        studentRepository.deleteAll();

        testFaculty = new Faculty();
        testFaculty.setName("TestFaculty");
        testFaculty.setColor("Red");
        facultyRepository.save(testFaculty);

        // Можно добавить студентов в факультет, если нужно
    }

    @Test
    void testGetFaculty() {
        String url = "http://localhost:" + port + "/faculty?id={id}";
        ResponseEntity<Faculty> response = restTemplate.getForEntity(url, Faculty.class, testFaculty.getId());

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(response.hasBody());
        Assertions.assertEquals(testFaculty.getId(), response.getBody().getId());
        Assertions.assertEquals("TestFaculty", response.getBody().getName());
    }

    @Test
    void testGetAllFaculties() {
        String url = "http://localhost:" + port + "/faculty/all";
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(url, Faculty[].class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(response.getBody().length >= 1);
    }

    @Test
    void testFindFacultiesByColor() {
        String url = "http://localhost:" + port + "/faculty/find?color=Red";
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(url, Faculty[].class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(Arrays.stream(response.getBody()).anyMatch(f -> f.getId().equals(testFaculty.getId())));
    }

    @Test
    void testFindFacultyByColorAndName() {
        String url = "http://localhost:" + port + "/faculty/findByColor?color=Red&name=TestFaculty";
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(url, Faculty[].class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(Arrays.stream(response.getBody()).anyMatch(f -> f.getId().equals(testFaculty.getId())));
    }

    @Test
    void testFindStudentsInFaculty() {
        String url = "http://localhost:" + port + "/faculty/findStudentsInFaculty?id={id}";
        ResponseEntity<Student[]> response = restTemplate.getForEntity(url, Student[].class, testFaculty.getId());
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        // Можно проверить, что список не пуст, если добавляли студентов
    }

    @Test
    void testCreateFaculty() {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("NewFaculty");
        newFaculty.setColor("Blue");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                newFaculty,
                Faculty.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertEquals("NewFaculty", response.getBody().getName());
    }

    @Test
    void testEditFaculty() {
        Faculty update = new Faculty();
        update.setId(testFaculty.getId());
        update.setName("UpdatedName");
        update.setColor("Green");
        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                new HttpEntity<>(update),
                Faculty.class
        );
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("UpdatedName", response.getBody().getName());
    }

    @Test
    void testDeleteFaculty() {
        String url = "http://localhost:" + port + "/faculty?id={id}";
        restTemplate.delete(url, testFaculty.getId());
        Optional<Faculty> deleted = facultyRepository.findById(testFaculty.getId());
        Assertions.assertFalse(deleted.isPresent());
    }
}
