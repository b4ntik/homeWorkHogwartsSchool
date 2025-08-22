package ru.hogwarts.school.controller;


import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.service.StudentService;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public String testApi() {return "Welcome to Demo!";};

    @GetMapping("{id}")
    public Book getBook(@PathVariable long id){return studentService.findStudent(id);}
}
