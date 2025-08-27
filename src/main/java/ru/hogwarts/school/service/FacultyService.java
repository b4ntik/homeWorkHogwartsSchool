package ru.hogwarts.school.service;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class FacultyService {
    @Autowired
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository){ this.facultyRepository = facultyRepository;}

    //создание факультета
    @Transactional
    public Faculty createFaculty(@Nullable Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);

    }

    //изменение факультета
    @Transactional
    public Optional<Faculty> editFaculty(Faculty faculty) {
        if (faculty.getId() != null) {
        Optional<Faculty> editedFaculty = facultyRepository.findById(faculty.getId());
            if(editedFaculty.isPresent()){
                Faculty newFaculty = editedFaculty.get();
                newFaculty.setName(faculty.getName());
                newFaculty.setColor(faculty.getColor());
                facultyRepository.save(newFaculty);
                return Optional.of(newFaculty);
            }
        }
        return Optional.empty();
    }

    //удаление факультета
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    //найти факультет по айди
    public Optional<Faculty> findFaculty(long id) {

            return facultyRepository.findById(id);
    }

    //выдать все факультеты

    public Collection<Faculty> getAllFaculties() {

        return  facultyRepository.findAll();

    }
    //фильтр факультетов по цвету
    public Collection<Faculty> findFacultiesByColor(String color) {

        return facultyRepository.findByColor(color);

    }
    public Collection<Faculty> findFacultyByColorOrNameIgnoreCase(String color, String name){

        return facultyRepository.findByColorOrNameIgnoreCase(color, name);
    }
}
