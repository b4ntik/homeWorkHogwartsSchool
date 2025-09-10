package ru.hogwarts.school;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.Collection;
import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class FacultyControllerTests {

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    private Faculty testFaculty, testFaculty2;


    @BeforeEach
    void setUp() {
        testFaculty = new Faculty();

        testFaculty.setName("TestFaculty");
        testFaculty.setColor("pink");
        testFaculty = facultyRepository.save(testFaculty);
        testFaculty2 = new Faculty();

        testFaculty2.setName("TestFaculty2");
        testFaculty2.setColor("pink");
        testFaculty2 = facultyRepository.save(testFaculty2);

    }
    //завел для отладки - не работало сохранение через createFaculty
    @Test
    void testSaveFacultyDirectly() {
        Faculty faculty = new Faculty();
        faculty.setName("Test");
        faculty.setColor("pink");

        Faculty saved = facultyRepository.save(faculty);
        Assertions.assertNotNull(saved.getId(), "ID должен быть сгенерирован");
    }
    @Test
    void testCreateFaculty() {

        HttpEntity<Faculty> request = new HttpEntity<>(testFaculty);

        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty", request, Faculty.class);

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Faculty createdFaculty = response.getBody();
        Assertions.assertNotNull(createdFaculty, "Тело ответа не должно быть null");
        Assertions.assertNotNull(createdFaculty.getId(), "ID должен быть сгенерирован");
    }
    @Test
    void getAllFaculties_shouldReturnListOfFaculties() {
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                "/faculty/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Collection<Faculty> faculties = response.getBody();
        System.out.println(faculties);

    }
    @Test
    void findFacultiesByColor_withColorParam() {
// для отладки
//        facultyRepository.save(testFaculty);
//        facultyRepository.save(testFaculty2);
//        facultyRepository.flush();
//        List<Faculty> allInDb = facultyRepository.findAll();
//        System.out.println("All faculties in DB: " + allInDb);
//        List<Faculty> pinkInDb = (List<Faculty>) facultyRepository.findByColor("pink");
//        System.out.println("Pink faculties in DB: " + pinkInDb);
        String color = "pink";

        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                "/faculty/find?color="+color,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Collection<Faculty> faculties = response.getBody();
        System.out.println(faculties);
        Assertions.assertNotNull(faculties);
        Assertions.assertEquals(2, faculties.size());
        Assertions.assertTrue(faculties.stream().allMatch(f -> color.equalsIgnoreCase((f.getColor()))));
        Set<Long> returnedIds = faculties.stream()
                .map(Faculty::getId)
                .collect(Collectors.toSet());

        Assertions.assertTrue(returnedIds.contains(testFaculty.getId()));
        Assertions.assertTrue(returnedIds.contains(testFaculty2.getId()));

    }
    @Test
    void findFacultyByColor_withColorParam_emptyList() {

        String color = "red";

        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                "/faculty/findByColor?color=" + color,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );


        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());

        Collection<Faculty> faculties = response.getBody();
        Assertions.assertEquals(0, faculties.size());
        Assertions.assertTrue(faculties.stream().allMatch(f -> color.equalsIgnoreCase(f.getColor())));
    }
    @Test
    void findFacultyByColor_withNameParam_shouldReturnFacultiesWithMatchingName() {

        String name = "TestFaculty";

       ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                "/faculty/findByColor?name=" + name,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());

        Collection<Faculty> faculties = response.getBody();
        Assertions.assertEquals(1, faculties.size());
        Assertions.assertTrue(faculties.stream().anyMatch(f -> name.equalsIgnoreCase(f.getName())));
    }
    @Test
    void findStudentsByFacultyId_withValidId_ReturnStudents() {
        Student student1 = new Student();
        student1.setName("Harry");
        student1.setAge(17);
        student1.setFaculty(testFaculty);

        Student student2 = new Student();
        student2.setName("Hermione");
        student2.setAge(17);
        student2.setFaculty(testFaculty);

        // сохраняем студентов
        studentRepository.saveAll(List.of(student1, student2));

        // запрос
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "/faculty/findStudentsInFaculty?id=" + testFaculty.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        // тесты
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());

        Collection<Student> students = response.getBody();
        Assertions.assertEquals(2, students.size());
        Faculty finalFaculty = testFaculty;
        Assertions.assertTrue(students.stream().allMatch(s -> finalFaculty.getId().equals(s.getFaculty().getId())));
    }
    @Test
    void testEditFaculty_success(){
        Faculty editedFaculty = new Faculty();
        editedFaculty.setId(2L);
        editedFaculty.setName("EditedFaculty");
        editedFaculty.setColor("white");

        HttpEntity<Faculty> request = new HttpEntity<>(editedFaculty);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "/faculty",
                HttpMethod.PUT,
                request,
                Faculty.class
                );

                Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
                Assertions.assertNotNull(response.getBody());
                Assertions.assertEquals("EditedFaculty", response.getBody().getName());
                Assertions.assertEquals("white", response.getBody().getColor());
    }
    @Test
    public void testDeleteFaculty_Success() {
        //удаляем факультет
        restTemplate.delete("/faculty?id=" + testFaculty.getId());
       // проверяем удаление
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "/faculty?id=" + testFaculty.getId(),
                Faculty.class
        );

        Assertions.assertNull(response.getBody());
    }
}
