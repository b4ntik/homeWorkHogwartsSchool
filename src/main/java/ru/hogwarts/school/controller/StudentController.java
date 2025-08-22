package ru.hogwarts.school.controller;


import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public String testApi() {return "Welcome to Demo!";};

    @GetMapping("{id}")
    public Student getStudent(@PathVariable long id){return studentService.findStudent(id);}
}
