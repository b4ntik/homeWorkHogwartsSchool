package ru.hogwarts.school.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


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

    @GetMapping("/student")
    public Optional<Student> getStudent(@RequestParam Long id) {
        return studentService.findStudent(id);
    }

    @GetMapping("/student/findByAge")
    public Collection<Student> getStudentBYAge(@RequestParam(required = false) int min, @RequestParam(required = false) int max) {
        return studentService.findStudentsByAgeBetween(min, max);
    }

    @GetMapping("/student/findFacultyByIdStudent")
    public Faculty getFacultyByStudentId(@RequestParam(required = false) Long studentId) {
        return studentService.findFacultyByStudentId(studentId);
    }


    @GetMapping ("/all")
    public ResponseEntity<Collection<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();

        return ResponseEntity.ok(students);
    }

    @GetMapping("/student/find")
    public ResponseEntity<Collection<Student>> findStudentsByAge(@RequestParam(required = false) int age){
        if (age<=0){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(studentService.findStudentsByAge(age));
    }

    @PostMapping("/student")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/student")
    public ResponseEntity<Optional<Student>> editStudent(@RequestBody Student student) {


        return ResponseEntity.ok( studentService.editStudent(student));
    }

    @DeleteMapping("/student")
    public void deleteStudent(@RequestParam Long id) {
        studentService.deleteStudent(id);
    }
}
