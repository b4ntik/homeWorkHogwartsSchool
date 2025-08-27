package ru.hogwarts.school.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;


import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RestController
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/faculty")
    public Optional<Faculty> getFaculty(@RequestParam long id) {
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
    @GetMapping("/faculty/findByColor")
    public ResponseEntity<Collection<Faculty>> findFacultyByColor(@RequestParam(required = false) String color, @RequestParam(required = false) String name) {
        if (color != null || name != null) {
            return ResponseEntity.ok(facultyService.findFacultyByColorOrNameIgnoreCase(color, name));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping("/faculty")
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping("/faculty")
    public ResponseEntity<Optional<Faculty>> editFaculty(@RequestBody Faculty faculty) {

        return ResponseEntity.ok(facultyService.editFaculty(faculty));
    }

    @DeleteMapping("/faculty")
    public void deleteFaculty(@RequestParam Long id) {
        facultyService.deleteFaculty(id);
    }
}
