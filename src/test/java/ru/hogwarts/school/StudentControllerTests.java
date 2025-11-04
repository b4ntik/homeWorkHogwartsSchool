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
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        testStudent = new Student();
        testStudent.setName("TestName");
        testStudent.setAge(20);
        testStudent = studentRepository.save(testStudent);
    }

    @Test
    void testGetStudent() {
        String url = "http://localhost:" + port + "/student?id={id}";
        ResponseEntity<Student> response = restTemplate.getForEntity(url, Student.class, testStudent.getId());

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(response.hasBody());
        Assertions.assertEquals(testStudent.getId(), response.getBody().getId());
        Assertions.assertEquals(testStudent.getName(), response.getBody().getName());
    }

    @Test
    void testGetStudentByAgeRange() {
        String url = "http://localhost:" + port + "/student/findByAge?min=10&max=30";
        ResponseEntity<Student[]> response = restTemplate.getForEntity(url, Student[].class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(response.getBody().length > 0);
        Assertions.assertTrue(
                Arrays.stream(response.getBody()).anyMatch(s -> s.getId().equals(testStudent.getId()))
        );
    }

    @Test
    void testGetAllStudents() {
        String url = "http://localhost:" + port + "/all";
        ResponseEntity<Student[]> response = restTemplate.getForEntity(url, Student[].class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(response.getBody().length >= 1);
    }

    @Test
    void testFindStudentsByAge() {
        String url = "http://localhost:" + port + "/student/find?age=20";
        ResponseEntity<Student[]> response = restTemplate.getForEntity(url, Student[].class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(
                Arrays.stream(response.getBody()).anyMatch(s -> s.getId().equals(testStudent.getId()))
        );
    }

    @Test
    void testCreateStudent() {
        Student newStudent = new Student();
        newStudent.setName("New Student");
        newStudent.setAge(25);
        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                newStudent,
                Student.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertEquals("New Student", response.getBody().getName());
    }

    @Test
    void testEditStudent() {
        Student updated = new Student();
        updated.setId(testStudent.getId());
        updated.setName("UpdatedName");
        updated.setAge(30);
        ResponseEntity<Student> response = restTemplate.exchange(
                "http://localhost:" + port + "/student",
                HttpMethod.PUT,
                new HttpEntity<>(updated),
                Student.class
        );
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("UpdatedName", response.getBody().getName());
    }

    @Test
    void testDeleteStudent() {
        String url = "http://localhost:" + port + "/student?id={id}";
        restTemplate.delete(url, testStudent.getId());
        Optional<Student> deleted = studentRepository.findById(testStudent.getId());
        Assertions.assertFalse(deleted.isPresent());
    }
}
