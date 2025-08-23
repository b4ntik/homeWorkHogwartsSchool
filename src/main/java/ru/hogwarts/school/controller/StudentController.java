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

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String testApi() {
        return "Welcome to Demo!";
    }

    ;

    @GetMapping("/students")
    public Student getStudent(@RequestParam long id) {
        return studentService.findStudent(id);
    }

    @GetMapping("/students/all")
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/students/find")
    public ResponseEntity<Collection<Student>> findStudentsByAge(@RequestParam(required = false) int age){
        if (age<=0){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(studentService.findStudentsByAge(age));
    }

    @PostMapping("/students")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/students")
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student changedStudent = studentService.editStudent(student);
        if (changedStudent == null) {
           return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(changedStudent);
    }

    @DeleteMapping("/students")
    public Student deleteStudent(@RequestParam long id) {
        return studentService.deleteStudent(id);
    }
}
