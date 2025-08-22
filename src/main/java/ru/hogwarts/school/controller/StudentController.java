package ru.hogwarts.school.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    public StudentController (StudentService studentService) { this.studentService = studentService;}

    @GetMapping
    public String testApi() {return "Welcome to Demo!";};

    @GetMapping("{id}")
    public Student getStudent(@PathVariable long id){return studentService.findStudent(id);}

    @GetMapping("/all")
    public ResponseEntity<Collection<Student>> getAllStudents(){
        return ResponseEntity.ok(studentService.getAlStudents());
    }

    @PostMapping
    public Student createStudent (@RequestBody Student student){
        return studentService.createStudent(student);
    }
}
