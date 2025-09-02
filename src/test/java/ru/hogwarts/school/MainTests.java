package ru.hogwarts.school;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainTests {
    @LocalServerPort
    private int port;

    @Autowired
    private AvatarController avatarController;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void avatarContrlollerLoads() throws Exception{
        Assertions.assertNotNull(avatarController);
    }
    @Test
    void facultyControllerLoads() throws Exception{
        Assertions.assertNotNull(facultyController);
    }
    @Test
    void studentControllerLoads() throws Exception{
        Assertions.assertNotNull(studentController);
    }

    @Test //Welcome to Demo!
    void testMainDefault() throws Exception {
        String url = "http://localhost:" + port +"/";
        String response = this.restTemplate.getForObject(url, String.class);
        Assertions.assertEquals("Welcome to Demo!", response);
    }
}
