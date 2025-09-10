package ru.hogwarts.school;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;

import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainTests {
    @LocalServerPort
    private int port;

    @Autowired
    private AvatarController avatarController;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    private TestStudent testStudent;
    private Student createdStudent;

    @BeforeEach
    void setUp() {
        testStudent = new TestStudent();
        createdStudent = studentService.createStudent(testStudent.getStudent());
    }


    @Test
    void avatarControllerLoads() throws Exception {
        Assertions.assertNotNull(avatarController);
    }

    @Test
    void facultyControllerLoads() throws Exception {
        Assertions.assertNotNull(facultyController);
    }

    @Test
        //тест главной  - Welcome to Demo!
    void testMainDefault() throws Exception {
        String url = "http://localhost:" + port + "/";
        String response = this.restTemplate.getForObject(url, String.class);
        Assertions.assertEquals("Welcome to Demo!", response);
    }
    //создание студента
    @Test
    void createStudent() throws Exception {

        Assertions.assertNotNull(createdStudent.getId());
        Assertions.assertEquals(createdStudent.getName(), "TestStudent");
        Assertions.assertEquals(createdStudent.getAge(), 66);

    }
    //изменить студента
    @Test
    void editStudent() throws Exception {

        createdStudent.setName("newTestStudent");
        createdStudent.setAge(45);

        Student editedStudent = studentService.editStudent(createdStudent);

        Assertions.assertNotNull(editedStudent);
        Assertions.assertEquals("newTestStudent", editedStudent.getName());
        Assertions.assertEquals(45, editedStudent.getAge());
    }
    //поиск студентов
    @Test
    void findStudent() throws Exception {

        Optional<Student> foundStudent = studentService.findStudent(createdStudent.getId());
        Assertions.assertEquals(foundStudent.get().getName(), "TestStudent");
    }
    //тест удаления студента
    @Test
    void testDeleteStudent() {

        studentService.deleteStudent(createdStudent.getId());
        Optional<Student> foundStudent = studentRepository.findById(createdStudent.getId());
        Assertions.assertFalse(foundStudent.isPresent());
    }
    //чистка БД от тестовых сущностей
    @Test
    void cleanDataBaseAfterTests() throws Exception {
        studentService.deleteStudentsByName("TestStudent");
        Collection<Student> foundStudents = studentRepository.findStudentsByName("TestStudent");
        Assertions.assertTrue(foundStudents.isEmpty());
    }
}
