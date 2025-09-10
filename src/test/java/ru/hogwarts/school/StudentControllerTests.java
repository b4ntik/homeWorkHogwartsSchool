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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTests {
    @LocalServerPort
    private int port;

   @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    private Student testStudent, testStudent2;
    private Student createdStudent;
    private Faculty testFaculty;

    @BeforeEach
    void setUp() {

        testStudent = new Student();
        //testStudent.setId(1L);
        testStudent.setName("TestStudent");
        testStudent.setAge(66);
        testStudent2 = new Student();
       // testStudent2.setId(2L);
        testStudent2.setName("TestStudent2");
        testStudent2.setAge(99);
        testFaculty = new Faculty();
       //testFaculty.setId(1L);
        testFaculty.setName("Slizereen");
        testFaculty.setColor("green");
        createdStudent = testStudent;
    }
@Test
    void testGetStudent(){
        Long studentId = 1L;

        //when(studentService.findStudent(studentId)).thenReturn(Optional.of(testStudent));

    ResponseEntity<Student> response = restTemplate.getForEntity("/student?id={id}",Student.class, studentId);

     Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
     Assertions.assertTrue(response.hasBody());
     Assertions.assertTrue(response.getBody().getId().equals(studentId));
     Assertions.assertTrue(response.getBody().getName().equals(testStudent.getName()));
}
@Test
    void testGetStudentByAge(){
        int min = 60;
        int max = 100;

        String url = "http://localhost:" + port + "/student/findByAge?min=" + min + "&max=" +max;

    when(studentService.findStudentsByAgeBetween(min, max)).thenReturn(List.of(testStudent, testStudent2));

        ResponseEntity<List<Student>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }
        );
        List<Student> students= response.getBody();
        List<Student> expected = List.of(testStudent, testStudent2);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        // для отладки
        System.out.println("Students: " + students);
        System.out.println("Expected: " + expected);
        Assertions.assertTrue(students.containsAll(expected));
}
    @Test
    void testGetFacultyByStudentId() {
        Long studentId = 1L;

        // vокаем поведение сервиса
        when(studentService.findFacultyByStudentId(studentId)).thenReturn(testFaculty);

        // запрос с параметром studentId
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/student/findFacultyByIdStudent?studentId={id}",
                Faculty.class,
                studentId);

        // проверяю статус и тело ответа
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(testFaculty.getId(), response.getBody().getId());
        Assertions.assertEquals(testFaculty.getName(), response.getBody().getName());

        // проверяю, что сервис вызвался с правильным параметром
        verify(studentService).findFacultyByStudentId(studentId);
    }
@Test
    void getAllStudentsTest(){
    List<Student> mockStudents = List.of(testStudent, testStudent2);

    // мокаем сервис
    when(studentService.getAllStudents()).thenReturn(mockStudents);

    // запрос к /all
    ResponseEntity<Student[]> response = restTemplate.getForEntity("/all", Student[].class);


    Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
    Student[] students = response.getBody();
    Assertions.assertNotNull(students);
    Assertions.assertEquals(2, students.length);
    Assertions.assertEquals(testStudent.getId(), students[0].getId());
    Assertions.assertEquals(testStudent.getName(), students[0].getName());
    Assertions.assertEquals(testStudent2.getId(), students[1].getId());
    Assertions.assertEquals(testStudent2.getName(), students[1].getName());


    verify(studentService).getAllStudents();
}
    @Test
    void testFindStudentsByAge_validAge() {
        int age = 66;
        List<Student> mockStudents = List.of(testStudent);

        // мок сервиса
        when(studentService.findStudentsByAge(age)).thenReturn(mockStudents);

        // запрос на поиск студента
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "/student/find?age={age}",
                Student[].class,
                age);

        //проверка статуса
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Student[] students = response.getBody();
        Assertions.assertNotNull(students);
        Assertions.assertEquals(1, students.length);

        // содержимое
        Assertions.assertEquals(testStudent.getId(), students[0].getId());
        Assertions.assertEquals(testStudent.getName(), students[0].getName());

        // проверяем вызов сервиса с правильным параметром
        verify(studentService).findStudentsByAge(age);
    }
    @Test
    void testCreateStudent() {
        // мок сервиса
        when(studentService.createStudent(testStudent)).thenReturn(createdStudent);

        // запрос с телом Student
        ResponseEntity<Student> response = restTemplate.postForEntity(
                "/student",
                testStudent,
                Student.class);

        // статус 200 OK
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());

        // проверяем тело ответа
        Student responseStudent = response.getBody();
        Assertions.assertNotNull(responseStudent);
        Assertions.assertEquals(createdStudent.getId(), responseStudent.getId());
        Assertions.assertEquals(createdStudent.getName(), responseStudent.getName());
        Assertions.assertEquals(createdStudent.getAge(), responseStudent.getAge());

        // проверяем, что сервис вызвался с правильным аргументом
        verify(studentService).createStudent(testStudent);
    }
    @Test
    void testEditStudent() {

        // мок сервиса
        when(studentService.editStudent(testStudent)).thenReturn(Optional.ofNullable(testStudent2));

        // запрос с телом
        HttpEntity<Student> requestEntity = new HttpEntity<>(testStudent);
        ResponseEntity<Optional> response = restTemplate.exchange(
                "/student",
                HttpMethod.PUT,
                requestEntity,
                Optional.class);


        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        // Проверяем, что сервис вызвался с правильным аргументом
        verify(studentService).editStudent(testStudent);
    }
    @Test
    void testDeleteStudent() {
        Long studentId = 1L;
        // запрос с параметром id
        ResponseEntity<Void> response = restTemplate.exchange(
                "/student?id=" + studentId,
                HttpMethod.DELETE,
                null,
                Void.class);

          Assertions.assertTrue(
                response.getStatusCode() == HttpStatus.OK ||
                        response.getStatusCode() == HttpStatus.NO_CONTENT);

        // сервис вызвался с нужным id
        verify(studentService).deleteStudent(studentId);
    }
}
