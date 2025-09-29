package ru.hogwarts.school;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent, testStudent1, testStudent2, testStudent3, testStudent4;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        testStudent = new Student();
        testStudent.setName("TestName1");
        testStudent.setAge(21);
        testStudent = studentRepository.save(testStudent);
        testStudent1 = new Student();
        testStudent1.setName("TestName2");
        testStudent1.setAge(22);
        testStudent1 = studentRepository.save(testStudent1);
        testStudent2 = new Student();
        testStudent2.setName("TestName3");
        testStudent2.setAge(23);
        testStudent2 = studentRepository.save(testStudent2);
        testStudent3 = new Student();
        testStudent3.setName("TestName4");
        testStudent3.setAge(24);
        testStudent3 = studentRepository.save(testStudent3);
        testStudent4 = new Student();
        testStudent4.setName("TestName5");
        testStudent4.setAge(25);
        testStudent4 = studentRepository.save(testStudent4);
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
        String url = "http://localhost:" + port + "/student/find?age=21";
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
    @Test
    void testGetAllStudentsParallelPrint() {
        String url = "http://localhost:" + port + "/students/print-parallel";

        ResponseEntity<Collection<String>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<String>>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Collection<String> names = response.getBody();
        Assertions.assertNotNull(names);
        // проверка, что список содержит ожидаемые имена
        Assertions.assertTrue(names.contains("TestName1"));
        Assertions.assertTrue(names.contains("TestName2"));

    }
    @Test
    void testGetAllStudentsParallelPrintSynchronized() throws InterruptedException {
        String url = "http://localhost:" + port + "/students/print-synchronized";

        ResponseEntity<Collection<String>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<String>>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Collection<String> names = response.getBody();
        Assertions.assertNotNull(names);
        // проверки
        Assertions.assertTrue(names.contains("TestName3"));
        Assertions.assertTrue(names.contains("TestName4"));
    }

}
