package ru.hogwarts.school;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faculty testFaculty, testFaculty2;

    //чтобы не создавать в каждом тесте
    @BeforeEach
    void setUp() {
        testFaculty = new Faculty();
        testFaculty.setId(1L);
        testFaculty.setName("avadakedavra");
        testFaculty.setColor("pink");

        testFaculty2 = new Faculty();
        testFaculty2.setId(2L);
        testFaculty2.setName("luminos");
        testFaculty2.setColor("pink");

    }
    @Test
    public void testGetFaculty_found() throws Exception {

        // мок вызова сервиса (студент найден)
        when(facultyService.findFaculty(1L)).thenReturn(Optional.of(testFaculty));

        // Выполняем GET-запрос с параметром id=1 и проверяем результат
        mockMvc.perform(get("/faculty")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testFaculty)));
    }
    @Test
    public void testGetAllFaculties_found() throws Exception {

        List<Faculty> faculties = Arrays.asList(testFaculty, testFaculty2);

        // Мокаем вызов сервиса
        when(facultyService.getAllFaculties()).thenReturn(faculties);

        // Выполняем GET-запрос и проверяем результат
        mockMvc.perform(get("/faculty/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(faculties)));
    }
    @Test
    void getFacultyByColorTest_found() throws Exception {
        String findColor = "pink";

        Collection<Faculty> faculties = Arrays.asList(testFaculty);
        // Мокаем вызов сервиса
        when(facultyService.findFacultiesByColor(findColor)).thenReturn(faculties);

        mockMvc.perform(get("/faculty/find")
                        .param("color", String.valueOf(findColor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(faculties)));
    }
    @Test
    void getFacultyByColorOrNameTest_foundByColor() throws Exception {
        String findColor = "pink";

        Collection<Faculty> faculties = Arrays.asList(testFaculty, testFaculty2);
        // Мокаем вызов сервиса
        when(facultyService.findFacultyByColorOrNameIgnoreCase(findColor,null)).thenReturn(faculties);

        mockMvc.perform(get("/faculty/findByColor")
                        .param("color", findColor)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(faculties)));
    }
    @Test
    void getFacultyByColorOrNameTest_foundByName() throws Exception {

        String findName = testFaculty.getName();
        Collection<Faculty> faculties = Arrays.asList(testFaculty);
        // Мокаем вызов сервиса
        when(facultyService.findFacultyByColorOrNameIgnoreCase(null,findName)).thenReturn(faculties);

        mockMvc.perform(get("/faculty/findByColor")
                        .param("name", findName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(faculties)));
    }
    @Test
    void getFacultyByColorOrNameTest_WithoutParams() throws Exception {

        mockMvc.perform(get("/faculty/findByColor")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
@Test
    void testFindStudentsByFacultyId_withParams() throws Exception {
    Student s1 = new Student();
    Student s2 = new Student();
    s1.setAge(20);
    s1.setName("Bob");
    s1.setId(1L);
    s2.setAge(25);
    s2.setName("Alice");
    s2.setId(2L);
    Collection<Student> students = Arrays.asList(s1,s2);

    when(facultyService.findStudentByFacultyId(1L)).thenReturn(students);

    mockMvc.perform(get("/faculty/findStudentsInFaculty")
            .param("id","1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(students)));
}
@Test
    void testFindStudentsByFacultyId_withoutParams() throws Exception{
        mockMvc.perform(get("/faculty/finStudentsInFaculty")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
}
    @Test
    void createFacultyTest_created() throws Exception {
        Faculty inputFaculty = testFaculty;

        Faculty createdFaculty = testFaculty2;

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(createdFaculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputFaculty)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(createdFaculty)));
    }
    @Test
    void editFacultyTest_passed() throws Exception {
        Faculty editedFaculty = testFaculty2;

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(Optional.of(editedFaculty));

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFaculty)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(editedFaculty)));
    }
    @Test
    void deleteFacultyTest() throws Exception {
        Long facultyId = 1L;

        // Настраиваем мок, чтобы при вызове deleteStudent ничего не делать
        doNothing().when(facultyService).deleteFaculty(facultyId);

        mockMvc.perform(delete("/faculty")
                        .param("id", facultyId.toString()))
                .andExpect(status().isOk());

        // проверяю, что метод сервиса вызван ровно один раз с нужным айди
        verify(facultyService, times(1)).deleteFaculty(facultyId);
    }
}
