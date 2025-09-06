package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student testStudent, testStudent2;

    //чтобы не создавать в каждом тесте
    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setAge(22);
        testStudent.setName("Bob");

        testStudent2 = new Student();
        testStudent2.setId(2L);
        testStudent2.setAge(20);
        testStudent2.setName("Alice");

    }

    @Test
    public void testGetStudent_found() throws Exception {

        // мок вызова сервиса (студент найден)
        when(studentService.findStudent(1L)).thenReturn(Optional.of(testStudent));

        // Выполняем GET-запрос с параметром id=1 и проверяем результат
        mockMvc.perform(get("/student")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testStudent)));
    }

    @Test
    public void testGetStudent_notFound() throws Exception {

        // мок вызова сервиса (студент найден)
        when(studentService.findStudent(1L)).thenReturn(Optional.empty());

        // Выполняем GET-запрос с параметром id=1 и проверяем результат
        mockMvc.perform(get("/student")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getStudentByAgeTestBetween_found() throws Exception {

        Collection<Student> students = Arrays.asList(testStudent, testStudent2);

        int minAge = 18;
        int maxAge = 25;

        // Мокаем вызов сервиса
        when(studentService.findStudentsByAgeBetween(minAge, maxAge)).thenReturn(students);

        mockMvc.perform(get("/student/findByAge")
                        .param("min", String.valueOf(minAge))
                        .param("max", String.valueOf(maxAge))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }

    @Test
    void getStudentFacultyByStudentIdGet_found() throws Exception {
        Long studentId = 1L;

        Faculty faculty = new Faculty();
        faculty.setId(10L);
        faculty.setName("Slizereen");
        //мокаем вызов метода поиска
        when(studentService.findFacultyByStudentId(studentId)).thenReturn(faculty);

        mockMvc.perform(get("/student/findFacultyByIdStudent")
                        .param("studentId", studentId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(faculty)));
    }

    @Test
    public void testGetAllStudents_found() throws Exception {

        List<Student> students = Arrays.asList(testStudent, testStudent2);

        // Мокаем вызов сервиса
        when(studentService.getAllStudents()).thenReturn(students);

        // Выполняем GET-запрос и проверяем результат
        mockMvc.perform(get("/student/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }

    @Test
    void getStudentByAgeTest_found() throws Exception {
        int age = 22;

        Collection<Student> students = Arrays.asList(testStudent);
        // Мокаем вызов сервиса
        when(studentService.findStudentsByAge(age)).thenReturn(students);

        mockMvc.perform(get("/student/find")
                        .param("age", String.valueOf(age))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }

    @Test
    void createStudentTest_created() throws Exception {
        Student inputStudent = testStudent;

        Student createdStudent = testStudent;

        when(studentService.createStudent(any(Student.class))).thenReturn(createdStudent);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputStudent)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(createdStudent)));
    }

    @Test
    void editStudentTest_passed() throws Exception {
        Student editedStudent = testStudent2;

        when(studentService.editStudent(any(Student.class))).thenReturn(Optional.of(editedStudent));

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(editedStudent)));
    }

    @Test
    void deleteStudentTest() throws Exception {
        Long studentId = 1L;

        // Настраиваем мок, чтобы при вызове deleteStudent ничего не делать
        doNothing().when(studentService).deleteStudent(studentId);

        mockMvc.perform(delete("/student")
                        .param("id", studentId.toString()))
                .andExpect(status().isOk());

        // проверяю, что метод сервиса вызван ровно один раз с нужным айди
        verify(studentService, times(1)).deleteStudent(studentId);
    }
}




