package ru.hogwarts.school.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;


import java.util.Collection;
import java.util.Collections;

@RestController
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/faculty")
    public Faculty getFaculty(@RequestParam long id) {
        return facultyService.findFaculty(id);
    }

    @GetMapping("/faculty/all")
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @GetMapping("/faculty/find")
    public ResponseEntity<Collection<Faculty>> findFacultiesByColor(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findFacultiesByColor(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping("/faculty")
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping("/faculty")
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty changedFaculty = facultyService.editFaculty(faculty);
        if (changedFaculty == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(changedFaculty);
    }

    @DeleteMapping("/faculty")
    public Faculty deleteFaculty(@RequestParam long id) {
        return facultyService.deleteFaculty(id);
    }
}
